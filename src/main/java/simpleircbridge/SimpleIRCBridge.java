package simpleircbridge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod(value = SimpleIRCBridge.MODID)
public class SimpleIRCBridge {
	public static final String MODID = "simpleircbridge";
	public static final String VERSION = "1.14.4_1.2.0-dev";

	private static Logger logger = LogManager.getLogger(MODID);
	private SIBConfig sibConf;
	private BridgeIRCBot bot = null;
	private MinecraftServer mcServer;

	public SimpleIRCBridge() {
		logger.info("SIB constructing");
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.register(this); // fires ModConfigEvent
	}

	@SubscribeEvent
	public void preInit(FMLCommonSetupEvent event) {
		logger.info("SIB setting up");
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SIBConfig.SPEC);
	}

	@SubscribeEvent
	public void config(ModConfig.ModConfigEvent event) {
		logger.info("SIB receied config event");
		this.sibConf = new SIBConfig();
	}

	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		this.mcServer = event.getServer();
		if (this.bot != null) {
			throw new IllegalStateException("Tried to start 2 bots in one mod instance");
		}
		if (this.sibConf == null) {
			throw new IllegalStateException("Config not loaded");
		}
		this.bot = new BridgeIRCBot(this.sibConf, this);
		this.bot.run();

		MinecraftForge.EVENT_BUS.register(new GameEventHandler(this));
	}

	@SubscribeEvent
	public void serverStopping(FMLServerStoppingEvent event) {
		this.bot.disconnect();
	}

	@SubscribeEvent
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
			this.mcServer.getPlayerList().sendMessage(new StringTextComponent(line));
		}
	}

	/* package-private */ SIBConfig getSibConf() {
		return this.sibConf;
	}
}
