package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class GameEventHandler {
	private final SimpleIRCBridge bridge;

	public GameEventHandler(SimpleIRCBridge bridge) {
		this.bridge = bridge;
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGIN, SIBUtil.mangle(e.player.getDisplayNameString())));
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerLoggedOutEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGOUT, SIBUtil.mangle(e.player.getDisplayNameString())));
	}

	@SubscribeEvent
	public void command(CommandEvent e) {
		String nickname = SIBUtil.mangle(e.getSender().getDisplayName().getUnformattedText());
		/*
		 * Usually these would be instanceof checks, checking for
		 * net.minecraft.command.server.CommandEmote and
		 * net.minecraft.command.server.CommandBroadcast.
		 * 
		 * However, some mods insist on overriding commands with their own wrappers
		 * (looking at you, FTBUtilities) so we're checking the names here.
		 */
		if ("say".equals(e.getCommand().getCommandName())) {
			toIrc(String.format(FORMAT2_MC_BROADCAST, nickname, SIBUtil.join(" ", e.getParameters())));
		} else if ("me".equals(e.getCommand().getCommandName())) {
			toIrc(String.format(FORMAT2_MC_EMOTE, nickname, SIBUtil.join(" ", e.getParameters())));
		}
	}

	@SubscribeEvent
	public void serverChat(ServerChatEvent e) {
		toIrc(String.format(FORMAT2_MC_CHAT, SIBUtil.mangle(e.getPlayer().getDisplayNameString()), e.getMessage()));
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			toIrc(String.format(FORMAT1_MC_DEATH,
					e.getSource().getDeathMessage(e.getEntityLiving()).getUnformattedText()));
		}
	}

	private void toIrc(String s) {
		this.bridge.sendToIrc(s);
	}
}
