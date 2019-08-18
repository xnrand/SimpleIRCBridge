package simpleircbridge;

import static simpleircbridge.SIBConstants.*;

import java.net.InetSocketAddress;

import genericircbot.AbstractIRCBot;
import genericircbot.IRCConnectionInfo;
import utils.IRCMinecraftConverter;

public class BridgeIRCBot extends AbstractIRCBot {

	private SimpleIRCBridge bridge;

	/* package */ BridgeIRCBot(SimpleIRCBridge bridge) {

		super(//
				/* socketAddr = */ new InetSocketAddress((String) SIBConfig.IRC_HOSTNAME.get(), SIBConfig.IRC_PORT.get()), //
				/* useSsl = */ SIBConfig.IRC_TLS.get(), //
				/* info = */ new IRCConnectionInfo(
						/* nickname = */ !((String) SIBConfig.IRC_NICKNAME.get()).contains("(rnd)") ? ((String) SIBConfig.IRC_NICKNAME.get()) :
										((String) SIBConfig.IRC_NICKNAME.get()).replace("(rnd)", String.valueOf((int) (Math.random() * 100000))),
						/* username = */ (String) SIBConfig.IRC_USERNAME.get(),
						/* realname = */ (String) SIBConfig.IRC_REALNAME.get()),
						/* password = */ (String) SIBConfig.IRC_PASSWORD.get());
		SimpleIRCBridge.log().info((String) SIBConfig.IRC_HOSTNAME.get());
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
		if (SIBConfig.MC_FORMATTING.get()) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT2_IRC_PART, sender, reason));
	}

	@Override
	protected void onQuit(String sender, String reason) {
		if (SIBConfig.MC_FORMATTING.get()) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT2_IRC_QUIT, sender, reason));
	}

	@Override
	protected void onKick(String channel, String opsender, String victim, String reason) {
		if (SIBConfig.MC_FORMATTING.get()) {
			reason = IRCMinecraftConverter.convIRCtoMinecraft(reason);
		}
		toMc(String.format(FORMAT3_IRC_KICK, victim, opsender, reason));
	}

	@Override
	protected void onMessage(String channel, String sender, String message) {
		if (SIBConfig.MC_FORMATTING.get()) {
			message = IRCMinecraftConverter.convIRCtoMinecraft(message);
		}
		toMc(String.format(FORMAT2_IRC_CHAT, sender, message));

	}

	@Override
	protected void onAction(String channel, String sender, String action) {
		if (SIBConfig.MC_FORMATTING.get()) {
			action = IRCMinecraftConverter.convIRCtoMinecraft(action);
		}
		toMc(String.format(FORMAT2_IRC_EMOTE, sender, action));
	}

	@Override
	protected void onNickChange(String sender, String newnick) {
		toMc(String.format(FORMAT2_IRC_NICKCHG, sender, newnick));
	}

	@Override
	protected void onNumeric001() {
		joinChannel((String) SIBConfig.IRC_CHANNEL.get());
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
