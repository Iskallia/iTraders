package iskallia.itraders.init;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityCryoChamber;
import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.block.entity.TileEntityGraveStone;
import iskallia.itraders.block.render.TESRCryoChamber;
import iskallia.itraders.block.render.TESRGraveStone;
import iskallia.itraders.item.mesh.FighterEggMesh;
import iskallia.itraders.item.mesh.MinerEggMesh;
import iskallia.itraders.item.mesh.SkullNeckMesh;
import iskallia.itraders.item.mesh.TraderEggMesh;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class InitModel {

	public static void registerItemModels() {
		registerSimpleItemModel(InitItem.BIT_100, 0);
		registerSimpleItemModel(InitItem.BIT_500, 0);
		registerSimpleItemModel(InitItem.BIT_1000, 0);
		registerSimpleItemModel(InitItem.BIT_5000, 0);
		registerSimpleItemModel(InitItem.BIT_10000, 0);
		registerSimpleItemModel(InitItem.EGG_POUCH, 0);
		registerSimpleItemModel(InitItem.MAGIC_ORE_DUST, 0);
		registerSimpleItemModel(InitBlock.ITEM_GRAVE_STONE, 0);
		registerSimpleItemModel(InitBlock.ITEM_INFUSION_CAULDRON, 0);
		registerSimpleItemModel(InitItem.CRYO_CHAMBER, 0);
		registerSimpleItemModel(InitBlock.ITEM_GHOST_PEDESTAL, 0);

		registerBlockModel(InitBlock.INFUSION_CAULDRON, 0);
		registerBlockModel(InitBlock.GHOST_PEDESTAL, 0);

		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_TRADER, new TraderEggMesh(InitItem.SPAWN_EGG_TRADER));
		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_FIGHTER, new FighterEggMesh(InitItem.SPAWN_EGG_FIGHTER));
		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_MINER, new MinerEggMesh(InitItem.SPAWN_EGG_MINER));
		ModelLoader.setCustomMeshDefinition(InitItem.SKULL_NECKLACE, new SkullNeckMesh(InitItem.SKULL_NECKLACE));
	}
    
    public static void registerTileEntityRenderers() {
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGraveStone.class, new TESRGraveStone());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryoChamber.class, new TESRCryoChamber());
    }

    /* ---------------------------------- */

	private static void registerSimpleItemModel(Item item, int metadata) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void registerBlockModel(Block block, int metadata) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata,
				new ModelResourceLocation(Traders.getResource(block.getUnlocalizedName().substring(5)), "inventory"));
	}

}
