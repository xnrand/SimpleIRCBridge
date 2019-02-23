package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import utils.IRCMinecraftConverter;

@Mod.EventBusSubscriber(modid = SimpleIRCBridge.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GameEventHandler {
	private final SimpleIRCBridge bridge;

	public GameEventHandler(SimpleIRCBridge bridge) {
		this.bridge = bridge;
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGIN, SIBUtil.mangle(e.getPlayer().getGameProfile().getName())));
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerLoggedOutEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGOUT, SIBUtil.mangle(e.getPlayer().getGameProfile().getName())));
	}

	/*
	 * TODO 1.13 implementation for catching /me and /say.
	 */
//@formatter:off
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
//@formatter:on

	@SubscribeEvent
	public void serverChat(ServerChatEvent e) {
		String content = e.getMessage();
		if (this.bridge.getSibConf().ircFormatting) {
			content = IRCMinecraftConverter.convMinecraftToIRC(content);
		}
		toIrc(String.format(FORMAT2_MC_CHAT, SIBUtil.mangle(e.getPlayer().getGameProfile().getName()), content));
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			toIrc(String.format(FORMAT1_MC_DEATH,
					e.getSource().getDeathMessage(e.getEntityLiving()).getUnformattedComponentText()));
		}
	}

	private void toIrc(String s) {
		this.bridge.sendToIrc(s);
	}
}
