package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.item.mesh.FighterEggMesh;
import kaptainwutax.itraders.item.mesh.MinerEggMesh;
import kaptainwutax.itraders.item.mesh.TraderEggMesh;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

public class InitModel {

	public static void registerModels() {
		registerSimpleItemModel(InitItem.BIT_100, 0);
		registerSimpleItemModel(InitItem.BIT_500, 0);
		registerSimpleItemModel(InitItem.BIT_1000, 0);
		registerSimpleItemModel(InitItem.BIT_5000, 0);
		registerSimpleItemModel(InitItem.BIT_10000, 0);
		registerSimpleItemModel(InitItem.EGG_POUCH, 0);
		registerSimpleItemModel(InitItem.SKULL_NECKLACE, 0);
		registerSimpleItemModel(InitItem.MAGIC_ORE_DUST, 0);
		registerSimpleItemModel(InitBlock.ITEM_GRAVE_STONE, 0);
		registerSimpleItemModel(InitBlock.ITEM_INFUSION_CAULDRON, 0);

		registerBlockModel(InitBlock.INFUSION_CAULDRON, 0);

		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_TRADER, new TraderEggMesh(InitItem.SPAWN_EGG_TRADER));
		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_FIGHTER, new FighterEggMesh(InitItem.SPAWN_EGG_FIGHTER));
		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_MINER, new MinerEggMesh(InitItem.SPAWN_EGG_MINER));
	}

	private static void registerSimpleItemModel(Item item, int metadata) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void registerBlockModel(Block block, int metadata) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata,
				new ModelResourceLocation(Traders.getResource(block.getUnlocalizedName().substring(5)), "inventory"));
	}

}
