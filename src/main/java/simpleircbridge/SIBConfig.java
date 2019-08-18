package simpleircbridge;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class SIBConfig {

	private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec SERVER_CONFIG;

	public static final String CATEGORY_IRC = "irc";

	public static ForgeConfigSpec.ConfigValue IRC_NICKNAME;
	public static ForgeConfigSpec.ConfigValue IRC_USERNAME;
	public static ForgeConfigSpec.ConfigValue IRC_REALNAME;
	public static ForgeConfigSpec.ConfigValue IRC_PASSWORD;
	public static ForgeConfigSpec.ConfigValue IRC_HOSTNAME;
	public static ForgeConfigSpec.IntValue IRC_PORT;
	public static ForgeConfigSpec.ConfigValue IRC_CHANNEL;
	public static ForgeConfigSpec.BooleanValue IRC_TLS;

	public static final String CATEGORY_VISUAL = "visual";

	public static ForgeConfigSpec.BooleanValue IRC_FORMATTING;
	public static ForgeConfigSpec.BooleanValue MC_FORMATTING;

	static {
		SERVER_BUILDER.comment("IRC settings").push(CATEGORY_IRC);
		IRC_NICKNAME = SERVER_BUILDER.comment("The nickname that the relay bot will use. '(rnd)' will be replaced with up to 5 random digits.")
				.define("nick", "SIB(rnd)");
		IRC_USERNAME = SERVER_BUILDER.comment("The username/ident that the relay bot will use")
				.define("username", "sib");
		IRC_REALNAME = SERVER_BUILDER.comment("The realname/gecos that the relay bot will use")
				.define("realname", "Simple IRC Bridge");
		IRC_PASSWORD = SERVER_BUILDER.comment("IRC Server password (if any)")
				.define("password", "");
		IRC_HOSTNAME = SERVER_BUILDER.comment("Hostname or IP address of your IRC server")
				.define("hostname", "127.0.0.1");
		IRC_PORT = SERVER_BUILDER.comment("Port of the IRC server to connect to. Common values: 6697 for TLS/SSL; 6667 for plaintext connections")
				.defineInRange("port", 6697, 1025, 65535);
		IRC_CHANNEL = SERVER_BUILDER.comment("IRC channel to relay into")
				.define("channel", "#channel");
		IRC_TLS = SERVER_BUILDER.comment("Whether TLS/SSL is enabled. Set to 'false' for a plaintext connection")
				.define("tls", true);
		SERVER_BUILDER.pop();

		SERVER_BUILDER.comment("Visual settings").push(CATEGORY_VISUAL);
		IRC_FORMATTING = SERVER_BUILDER.comment("Whether minecraft formatting should be converted to IRC formatting")
				.define("ircFormatting", true);
		MC_FORMATTING = SERVER_BUILDER.comment("Whether IRC formatting should be converted to Minecraft formatting")
				.define("mcFormatting", true);
		SERVER_BUILDER.pop();

		SERVER_CONFIG = SERVER_BUILDER.build();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		configData.load();
		spec.setConfig(configData);
	}
}
