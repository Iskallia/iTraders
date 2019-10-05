package kaptainwutax.itraders.event;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.command.CommandITraders;
import kaptainwutax.itraders.gui.GuiHandler;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitEntity;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.init.InitTieredLoot;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class EventMod {
	
	public static void onConstruction(FMLConstructionEvent event) {
		
	}
	
	public static void onPreInitialization(FMLPreInitializationEvent event) {
		InitEntity.registerEntities();
		
		if(event.getSide() == Side.CLIENT) {
			InitEntity.registerEntityRenderers();
		}			
	}

	public static void onInitialization(FMLInitializationEvent event) {
		InitPacket.registerPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(Traders.getInstance(), new GuiHandler());
	}

	public static void onPostInitialization(FMLPostInitializationEvent event) {
		InitConfig.registerConfigs();
		InitTieredLoot.registerTiers();
	}

	public static void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandITraders());
	}
	
}
