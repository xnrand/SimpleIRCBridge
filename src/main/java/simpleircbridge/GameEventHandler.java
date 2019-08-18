package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import utils.IRCMinecraftConverter;

public class GameEventHandler {
	private final SimpleIRCBridge bridge;

	public GameEventHandler(SimpleIRCBridge bridge) {
		this.bridge = bridge;
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGIN, SIBUtil.mangle(e.getPlayer().getDisplayName().getString())));
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGOUT, SIBUtil.mangle(e.getPlayer().getDisplayName().getString())));
	}

//	@SubscribeEvent
//	public void command(CommandEvent e) {
//		String nickname = SIBUtil.mangle(e.getSender().getDisplayName().getUnformattedText());
//		/*
//		 * Usually these would be instanceof checks, checking for
//		 * net.minecraft.command.server.CommandEmote and
//		 * net.minecraft.command.server.CommandBroadcast.
//		 *
//		 * However, some mods insist on overriding commands with their own wrappers
//		 * (looking at you, FTBUtilities) so we're checking the names here.
//		 */
//
//		String content = SIBUtil.join(" ", e.getParameters());
//
//		if ("say".equals(e.getCommand().getName())) {
//			if (this.bridge.getSibConf().ircFormatting) {
//				content = IRCMinecraftConverter.convMinecraftToIRC(content);
//			}
//			toIrc(String.format(FORMAT2_MC_BROADCAST, nickname, content));
//
//		} else if ("me".equals(e.getCommand().getName())) {
//			if (this.bridge.getSibConf().ircFormatting) {
//				content = IRCMinecraftConverter.convMinecraftToIRC(content);
//			}
//			toIrc(String.format(FORMAT2_MC_EMOTE, nickname, content));
//		}
//	}

	@SubscribeEvent
	public void serverChat(ServerChatEvent e) {
		String content = e.getMessage();
		if (SIBConfig.IRC_FORMATTING.get()) {
			content = IRCMinecraftConverter.convMinecraftToIRC(content);
		}
		toIrc(String.format(FORMAT2_MC_CHAT, SIBUtil.mangle(e.getPlayer().getDisplayName().getString()), content));
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof PlayerEntity) {
			toIrc(String.format(FORMAT1_MC_DEATH,
					e.getSource().getDeathMessage(e.getEntityLiving()).getString()));
		}
	}

	private void toIrc(String s) {
		this.bridge.sendToIrc(s);
	}
}
