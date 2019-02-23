package simpleircbridge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/** class holds config structure, objects hold one config instance */
public class SIBConfig {
	private final static String CAT_IRC = "irc";

	private final static String KEY_ircNickname = "nick";
	private final static String COMMENT_ircNickname = "The nickname that the relay bot will use. '(rnd)' will be replaced with up to 5 random digits.";
	/* package */ final String ircNickname;
	private final static String DEFAULT_ircNickname = "SIB(rnd)";
	private final static ConfigValue<String> CONFIG_ircNickname;

	private final static String KEY_ircUsername = "username";
	private final static String COMMENT_ircUsername = "The username/ident that the relay bot will use";
	/* package */ final String ircUsername;
	private final static String DEFAULT_ircUsername = "sib";
	private final static ConfigValue<String> CONFIG_ircUsername;

	private final static String KEY_ircRealname = "realname";
	private final static String COMMENT_ircRealname = "The realname/gecos that the relay bot will use";
	/* package */final String ircRealname;
	private final static String DEFAULT_ircRealname = "Simple IRC Bridge";
	private final static ConfigValue<String> CONFIG_ircRealname;

	private final static String KEY_ircPassword = "password";
	private final static String COMMENT_ircPassword = "IRC Server password (if any)";
	/* package */ final String ircPassword;
	private final static String DEFAULT_ircPassword = "";
	private final static ConfigValue<String> CONFIG_ircPassword;

	private final static String KEY_ircHostname = "hostname";
	private final static String COMMENT_ircHostname = "Hostname or IP address of your IRC server";
	/* package */ final String ircHostname;
	private final static String DEFAULT_ircHostname = "127.0.0.1";
	private final static ConfigValue<String> CONFIG_ircHostname;

	private final static String KEY_ircChannel = "channel";
	private final static String COMMENT_ircChannel = "IRC channel to relay into";
	/* package */final String ircChannel;
	private final static String DEFAULT_ircChannel = "#channel";
	private final static ConfigValue<String> CONFIG_ircChannel;

	private final static String KEY_ircPort = "port";
	private final static String COMMENT_ircPort = "Port of the IRC server to connect to. Common values: 6697 for TLS/SSL; 6667 for plaintext connections";
	/* package */ final int ircPort;
	private final static int DEFAULT_ircPort = 6697;
	private final static ConfigValue<Integer> CONFIG_ircPort;
	private final static int MIN_ircPort = 1;
	private final static int MAX_ircPort = 65535;

	private final static String KEY_ircTLS = "tls";
	private final static String COMMENT_ircTLS = "Whether TLS/SSL is enabled. Set to 'false' for a plaintext connection";
	/* package */ final boolean ircTLS;
	private final static boolean DEFAULT_ircTLS = true;
	private final static ConfigValue<Boolean> CONFIG_ircTLS;

	private final static String KEY_ircFormatting = "ircFormatting";
	private final static String COMMENT_ircFormatting = "Whether minecraft formatting should be converted to IRC formatting.";
	/* package */ final boolean ircFormatting;
	private final static boolean DEFAULT_ircFormatting = true;
	private final static ConfigValue<Boolean> CONFIG_ircFormatting;

	private final static String KEY_mcFormatting = "mcFormatting";
	private final static String COMMENT_mcFormatting = "Whether IRC formatting should be converted to Minecraft formatting.";
	/* package */ final boolean mcFormatting;
	private final static boolean DEFAULT_mcFormatting = true;
	private final static ConfigValue<Boolean> CONFIG_mcFormatting;

	public static final ForgeConfigSpec SPEC;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push(CAT_IRC);
		CONFIG_ircNickname = builder.comment(COMMENT_ircNickname).define(KEY_ircNickname, DEFAULT_ircNickname);
		CONFIG_ircUsername = builder.comment(COMMENT_ircUsername).define(KEY_ircUsername, DEFAULT_ircUsername);
		CONFIG_ircRealname = builder.comment(COMMENT_ircRealname).define(KEY_ircRealname, DEFAULT_ircRealname);
		CONFIG_ircPassword = builder.comment(COMMENT_ircPassword).define(KEY_ircPassword, DEFAULT_ircPassword);
		CONFIG_ircHostname = builder.comment(COMMENT_ircHostname).define(KEY_ircHostname, DEFAULT_ircHostname);
		CONFIG_ircChannel = builder.comment(COMMENT_ircChannel).define(KEY_ircChannel, DEFAULT_ircChannel);
		CONFIG_ircPort = builder.comment(COMMENT_ircPort).defineInRange(KEY_ircPort, DEFAULT_ircPort, MIN_ircPort,
				MAX_ircPort);
		CONFIG_ircTLS = builder.comment(COMMENT_ircTLS).define(KEY_ircTLS, DEFAULT_ircTLS);
		CONFIG_ircFormatting = builder.comment(COMMENT_ircFormatting).define(KEY_ircFormatting, DEFAULT_ircFormatting);
		CONFIG_mcFormatting = builder.comment(COMMENT_mcFormatting).define(KEY_mcFormatting, DEFAULT_mcFormatting);
		builder.pop();
		SPEC = builder.build();
	}

	public SIBConfig() {
		String confNick = CONFIG_ircNickname.get();
		this.ircNickname = !confNick.contains("(rnd)") ? confNick
				: confNick.replace("(rnd)", String.valueOf((int) (Math.random() * 100000)));
		this.ircUsername = CONFIG_ircUsername.get();
		this.ircRealname = CONFIG_ircRealname.get();
		this.ircPassword = CONFIG_ircPassword.get();
		this.ircHostname = CONFIG_ircHostname.get();
		this.ircChannel = CONFIG_ircChannel.get();
		this.ircPort = CONFIG_ircPort.get();
		this.ircTLS = CONFIG_ircTLS.get();
		this.ircFormatting = CONFIG_ircFormatting.get();
		this.mcFormatting = CONFIG_mcFormatting.get();
	}
}
