package simpleircbridge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = SimpleIRCBridge.MODID, version = SimpleIRCBridge.VERSION, acceptableRemoteVersions = "*")
public class SimpleIRCBridge {
	public static final String MODID = "simpleircbridge";
	public static final String VERSION = "1.12.2_1.1.1-dev";

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
		MinecraftForge.EVENT_BUS.register(new GameEventHandler(this));
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
			this.mcServer.getPlayerList().sendMessage(new TextComponentString(line));
		}
	}

	/* package-private */ SIBConfig getSibConf() {
		return this.sibConf;
	}
}
