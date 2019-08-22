package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.brigadier.ParseResults;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import utils.IRCMinecraftConverter;

@Mod.EventBusSubscriber(modid = SimpleIRCBridge.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GameEventHandler {
	private static final Pattern CHAT_CMD_PTTRN = Pattern.compile("^/?(me|say)\\s+(.*)$");

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

	@SubscribeEvent
	/* TODO With Brigadier, this is nowhere near as nice as it used to be. */
	/* TODO This doesn't check if the player is allowed to run /me or /say */
	public void command(CommandEvent e) {
		ParseResults<CommandSource> cmd = e.getParseResults();
		String nickname;

		CommandSource source = cmd.getContext().getSource();
		if (source.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();
			nickname = SIBUtil.mangle(player.getGameProfile().getName());
		} else {
			nickname = source.getDisplayName().getString();
		}

		Matcher cmdAndArgs = CHAT_CMD_PTTRN.matcher(cmd.getReader().getString());
		if (!cmdAndArgs.matches()) {
			return;
		}
		String content = cmdAndArgs.group(2);

		if ("say".equals(cmdAndArgs.group(1))) {
			if (this.bridge.getSibConf().ircFormatting) {
				content = IRCMinecraftConverter.convMinecraftToIRC(content);
			}
			toIrc(String.format(FORMAT2_MC_BROADCAST, nickname, content));

		} else if ("me".equals(cmdAndArgs.group(1))) {
			if (this.bridge.getSibConf().ircFormatting) {
				content = IRCMinecraftConverter.convMinecraftToIRC(content);
			}
			toIrc(String.format(FORMAT2_MC_EMOTE, nickname, content));
		}
	}

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
		if (e.getEntityLiving() instanceof PlayerEntity) {
			toIrc(String.format(FORMAT1_MC_DEATH,
					e.getSource().getDeathMessage(e.getEntityLiving()).getString()));
		}
	}

	private void toIrc(String s) {
		this.bridge.sendToIrc(s);
	}
}
