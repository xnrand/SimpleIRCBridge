package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import java.net.InetSocketAddress;

import genericircbot.AbstractIRCBot;
import genericircbot.IRCConnectionInfo;
import utils.IRCMinecraftConverter;

public class BridgeIRCBot extends AbstractIRCBot {

	private SimpleIRCBridge bridge;

	/* package */ BridgeIRCBot(SIBConfig conf, SimpleIRCBridge bridge) {
		super(//
				/* socketAddr = */ new InetSocketAddress(conf.ircHostname, conf.ircPort), //
				/* useSsl = */ conf.ircTLS, //
				/* info = */ new IRCConnectionInfo(conf.ircNickname, conf.ircUsername, conf.ircRealname), //
				/* servPassword = */ conf.ircPassword);
		this.bridge = bridge;
	}

	@Override
	protected void logMessage(String msg) {
		SimpleIRCBridge.log().info(msg);
	}

	/* event handling */
	@Override
	protected void onJoin(String channel, String sender) {
		toMc(String.format(FORMAT1_IRC_JOIN, sender));
	}

	@Override
	protected void onPart(String channel, String sender, String reason) {
		if (this.bridge.getSibConf().mcFormatting) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT2_IRC_PART, sender, reason));
	}

	@Override
	protected void onQuit(String sender, String reason) {
		if (this.bridge.getSibConf().mcFormatting) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT2_IRC_QUIT, sender, reason));
	}

	@Override
	protected void onKick(String channel, String opsender, String victim, String reason) {
		if (this.bridge.getSibConf().mcFormatting) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT3_IRC_KICK, victim, opsender, reason));
	}

	@Override
	protected void onMessage(String channel, String sender, String message) {
		if (this.bridge.getSibConf().mcFormatting) {
			message = IRCMinecraftConverter.convIRCtoMinecraft(message);
		}
		toMc(String.format(FORMAT2_IRC_CHAT, sender, message));

	}

	@Override
	protected void onAction(String channel, String sender, String action) {
		if (this.bridge.getSibConf().mcFormatting) {
			action = IRCMinecraftConverter.convIRCtoMinecraft(action);
		}
		toMc(String.format(FORMAT2_IRC_EMOTE, sender, action));
	}

	@Override
	protected void onNickChange(String sender, String newnick) {
		toMc(String.format(FORMAT2_IRC_NICKCHG, sender, newnick));
	}

	@Override
	protected void onConnect() {
		joinChannel(this.bridge.getSibConf().ircChannel);
	}

	/** {@inheritDoc} */ // re-declare protected, publish method for package
	@Override
	protected void disconnect() {
		super.disconnect();
	}

	/** {@inheritDoc} */ // re-declare protected, publish method for package
	@Override
	protected void kill() {
		super.kill();
	}

	/** {@inheritDoc} */ // re-declare protected, publish method for package
	@Override
	protected void sendMessage(String channel, String message) {
		super.sendMessage(channel, message);
	}

	private void toMc(String s) {
		this.bridge.sendToMinecraft(s);
	}
}
