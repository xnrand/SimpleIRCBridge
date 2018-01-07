package simpleircbridge;

import net.minecraftforge.common.config.Configuration;

/** class holds config structure, objects hold one config instance */
public class SIBConfig {
	private final static String CAT_IRC = "irc";

	private final static String KEY_ircNickname = "nick";
	private final static String COMMENT_ircNickname = "The nickname that the relay bot will use. '(rnd)' will be replaced with up to 5 random digits.";
	/* package */ final String ircNickname;
	private final static String DEFAULT_ircNickname = "SIB(rnd)";

	private final static String KEY_ircUsername = "username";
	private final static String COMMENT_ircUsername = "The username/ident that the relay bot will use";
	/* package */ final String ircUsername;
	private final static String DEFAULT_ircUsername = "sib";

	private final static String KEY_ircRealname = "realname";
	private final static String COMMENT_ircRealname = "The realname/gecos that the relay bot will use";
	/* package */final String ircRealname;
	private final static String DEFAULT_ircRealname = "Simple IRC Bridge";

	private final static String KEY_ircPassword = "password";
	private final static String COMMENT_ircPassword = "IRC Server password (if any)";
	/* package */ final String ircPassword;
	private final static String DEFAULT_ircPassword = "";

	private final static String KEY_ircHostname = "hostname";
	private final static String COMMENT_ircHostname = "Hostname or IP address of your IRC server";
	/* package */ final String ircHostname;
	private final static String DEFAULT_ircHostname = "127.0.0.1";

	private final static String KEY_ircChannel = "channel";
	private final static String COMMENT_ircChannel = "IRC channel to relay into";
	/* package */final String ircChannel;
	private final static String DEFAULT_ircChannel = "#channel";

	private final static String KEY_ircPort = "port";
	private final static String COMMENT_ircPort = "Port of the IRC server to connect to. Common values: 6697 for TLS/SSL; 6667 for plaintext connections";
	/* package */ final int ircPort;
	private final static int DEFAULT_ircPort = 6697;
	private final static int MIN_ircPort = 1;
	private final static int MAX_ircPort = 65535;

	private final static String KEY_ircTLS = "tls";
	private final static String COMMENT_ircTLS = "Whether TLS/SSL is enabled. Set to 'false' for a plaintext connection";
	/* package */ final boolean ircTLS;
	private final static boolean DEFAULT_ircTLS = true;

	/** gets all SIB properties. load/save is needs to be handled by caller */
	public SIBConfig(Configuration conf) {
		String confNick = conf.getString(KEY_ircNickname, CAT_IRC, DEFAULT_ircNickname, COMMENT_ircNickname);
		this.ircNickname = !confNick.contains("(rnd)") ? confNick //
				: confNick.replace("(rnd)", String.valueOf((int) (Math.random() * 100000)));
		this.ircUsername = conf.getString(KEY_ircUsername, CAT_IRC, DEFAULT_ircUsername, COMMENT_ircUsername);
		this.ircRealname = conf.getString(KEY_ircRealname, CAT_IRC, DEFAULT_ircRealname, COMMENT_ircRealname);
		this.ircPassword = conf.getString(KEY_ircPassword, CAT_IRC, DEFAULT_ircPassword, COMMENT_ircPassword);
		this.ircHostname = conf.getString(KEY_ircHostname, CAT_IRC, DEFAULT_ircHostname, COMMENT_ircHostname);
		this.ircChannel = conf.getString(KEY_ircChannel, CAT_IRC, DEFAULT_ircChannel, COMMENT_ircChannel);
		this.ircPort = conf.getInt(KEY_ircPort, CAT_IRC, DEFAULT_ircPort, MIN_ircPort, MAX_ircPort, COMMENT_ircPort);
		this.ircTLS = conf.getBoolean(KEY_ircTLS, CAT_IRC, DEFAULT_ircTLS, COMMENT_ircTLS);
		conf.setCategoryRequiresMcRestart(CAT_IRC, true);
	}
}
