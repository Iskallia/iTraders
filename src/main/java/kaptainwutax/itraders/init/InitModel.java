package kaptainwutax.itraders.init;

import kaptainwutax.itraders.item.mesh.FighterEggMesh;
import kaptainwutax.itraders.item.mesh.TraderEggMesh;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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

		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_TRADER, new TraderEggMesh(InitItem.SPAWN_EGG_TRADER));
		ModelLoader.setCustomMeshDefinition(InitItem.SPAWN_EGG_FIGHTER, new FighterEggMesh(InitItem.SPAWN_EGG_FIGHTER));
	}

	private static void registerSimpleItemModel(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
