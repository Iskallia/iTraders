package iskallia.itraders.event;

import iskallia.itraders.Traders;
import iskallia.itraders.command.CommandGiveBits;
import iskallia.itraders.command.CommandITraders;
import iskallia.itraders.gui.GuiHandler;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitEntity;
import iskallia.itraders.init.InitModel;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.init.InitTieredLoot;
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

		if (event.getSide() == Side.CLIENT) {
			InitEntity.registerEntityRenderers();
			InitModel.registerTileEntityRenderers();
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
		event.registerServerCommand(new CommandGiveBits());
	}

}
