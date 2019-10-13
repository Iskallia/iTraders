package kaptainwutax.itraders;

import kaptainwutax.itraders.event.EventMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Traders.MOD_ID, name = Traders.MOD_NAME, version = Traders.MOD_VERSION)
public class Traders {

	@Mod.Instance
	private static Traders INSTANCE;

	public static final String MOD_ID = "itraders";
	public static final String MOD_NAME = "iTraders";
	public static final String MOD_VERSION = "0.2.11";

	public static Traders getInstance() {
		return INSTANCE;
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

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(Traders.MOD_ID, name);
	}

}
