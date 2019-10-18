package kaptainwutax.itraders.event;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitBlock;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.init.InitModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Traders.MOD_ID)
public class EventRegistry {

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		InitBlock.registerBlocks(event.getRegistry());
		InitBlock.registerTileEntities();
	}
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		InitItem.registerItems(event.getRegistry());
		InitBlock.registerItemBlocks(event.getRegistry());
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		InitModel.registerModels();
	}

}
