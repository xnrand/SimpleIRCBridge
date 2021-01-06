package simpleircbridge;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
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
	public static final String VERSION = "1.16.4_1.2.1";
	private static final UUID IRC_UUID = UUID.nameUUIDFromBytes(MODID.getBytes(StandardCharsets.US_ASCII));

	private static Logger logger = LogManager.getLogger(MODID);
	private SIBConfig sibConf;
	private BridgeIRCBot bot = null;
	private MinecraftServer mcServer;

	public SimpleIRCBridge() {
		logger.info("SIB constructing");
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::preInit);
		bus.addListener(this::config);
	}

	/** registered manually in the ModLoadingContext {@linkplain SimpleIRCBridge#SimpleIRCBridge() here} */
	public void preInit(FMLCommonSetupEvent event) {
		logger.info("SIB setting up");
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SIBConfig.SPEC);
	}

	/** registered manually in the ModLoadingContext {@linkplain SimpleIRCBridge#SimpleIRCBridge() here} */
	public void config(ModConfig.ModConfigEvent event) {
		if (event.getConfig().getSpec() == SIBConfig.SPEC) {
			logger.info("SIB receied related config event");
			this.sibConf = new SIBConfig();
		} else {
			logger.info("SIB receied unrelated config event");
		}
	}

	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		logger.info("SIB received server starting event");
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
		logger.info("SIB received server stopping event");
		this.bot.disconnect();
	}

	@SubscribeEvent
	public void serverStopped(FMLServerStoppedEvent event) {
		logger.info("SIB received server stopped event");
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
			this.mcServer.getPlayerList().func_232641_a_(new StringTextComponent(line), ChatType.CHAT, IRC_UUID);
		}
	}

	/* package-private */ SIBConfig getSibConf() {
		return this.sibConf;
	}
}
