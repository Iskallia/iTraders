package iskallia.itraders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iskallia.itraders.event.EventMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Traders.MOD_ID, name = Traders.MOD_NAME, version = Traders.MOD_VERSION)
public class Traders {

	public static final Logger LOG = LogManager.getLogger(Traders.MOD_NAME);

	@Mod.Instance
	private static Traders INSTANCE;

	public static final String MOD_ID = "itraders";
	public static final String MOD_NAME = "iTraders";
	public static final String MOD_VERSION = "${version}";

	public static Traders getInstance() {
		return INSTANCE;
	}	

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Traders.MOD_ID, name);
	}

	@Mod.EventHandler
	public void onConstruction(FMLConstructionEvent event) {
		EventMod.onConstruction(event);
	}

	@Mod.EventHandler
	public void onPreInitialization(FMLPreInitializationEvent event) {
		EventMod.onPreInitialization(event);
	}

	@Mod.EventHandler
	public void onInitialization(FMLInitializationEvent event) {
		EventMod.onInitialization(event);
	}

	@Mod.EventHandler
	public void onPostInitialization(FMLPostInitializationEvent event) {
		EventMod.onPostInitialization(event);
	}

	@Mod.EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		EventMod.onServerStart(event);
	}

}
