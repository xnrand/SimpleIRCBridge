package simpleircbridge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = SimpleIRCBridge.MODID, version = SimpleIRCBridge.VERSION, acceptableRemoteVersions = "*")
public class SimpleIRCBridge {
	public static final String MODID = "simpleircbridge";
	public static final String VERSION = "1.7.10_1.1.1-dev";

	private static Logger logger = LogManager.getLogger();
	private SIBConfig sibConf;
	private Configuration fmlConf;
	private BridgeIRCBot bot;
	private MinecraftServer mcServer;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		this.fmlConf = new Configuration(event.getSuggestedConfigurationFile());
		this.fmlConf.load();
		this.sibConf = new SIBConfig(this.fmlConf);
		this.fmlConf.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("sib init");
		GameEventHandler geh = new GameEventHandler(this);
		MinecraftForge.EVENT_BUS.register(geh);
		FMLCommonHandler.instance().bus().register(geh);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		this.mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		this.bot = new BridgeIRCBot(this.sibConf, this);
		this.bot.run();
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		this.bot.disconnect();
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		this.bot.kill();
		this.bot = null;
		this.mcServer = null;
	}

	/* package-private */ static Logger log() {
		return logger;
	}

	/* package-private */ void sendToIrc(String line) {
		if (this.bot != null) {
			this.bot.sendMessage(this.sibConf.ircChannel, line);
		}
	}

	/* package-private */ void sendToMinecraft(String line) {
		if (this.mcServer != null) {
			this.mcServer.getConfigurationManager().sendChatMsg(new ChatComponentText(line));
		}
	}

	/* package-private */ SIBConfig getSibConf() {
		return this.sibConf;
	}
}
