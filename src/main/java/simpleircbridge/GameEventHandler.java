package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class GameEventHandler {
	private final SimpleIRCBridge bridge;

	public GameEventHandler(SimpleIRCBridge bridge) {
		this.bridge = bridge;
	}

	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGIN, SIBUtil.mangle(e.player.getDisplayName())));
	}

	@SubscribeEvent
	public void playerLoggedOut(PlayerLoggedOutEvent e) {
		toIrc(String.format(FORMAT1_MC_LOGOUT, SIBUtil.mangle(e.player.getDisplayName())));
	}

	@SubscribeEvent
	public void command(CommandEvent e) {
		String nickname = SIBUtil.mangle(e.sender.getCommandSenderName());
		/*
		 * Usually these would be instanceof checks, checking for
		 * net.minecraft.command.server.CommandEmote and
		 * net.minecraft.command.server.CommandBroadcast.
		 * 
		 * However, some mods insist on overriding commands with their own wrappers
		 * (looking at you, FTBUtilities) so we're checking the names here.
		 */
		if ("say".equals(e.command.getCommandName())) {
			toIrc(String.format(FORMAT2_MC_BROADCAST, nickname, SIBUtil.join(" ", e.parameters)));
		} else if ("me".equals(e.command.getCommandName())) {
			toIrc(String.format(FORMAT2_MC_EMOTE, nickname, SIBUtil.join(" ", e.parameters)));
		}
	}

	@SubscribeEvent
	public void serverChat(ServerChatEvent e) {
		toIrc(String.format(FORMAT2_MC_CHAT, SIBUtil.mangle(e.player.getDisplayName()), e.message));
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent e) {
		if (e.entityLiving instanceof EntityPlayer) {
			toIrc(String.format(FORMAT1_MC_DEATH,
					e.source.func_151519_b(e.entityLiving).getUnformattedText()));
		}
	}

	private void toIrc(String s) {
		this.bridge.sendToIrc(s);
	}
}
