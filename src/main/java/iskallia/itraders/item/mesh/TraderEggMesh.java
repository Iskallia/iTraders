package iskallia.itraders.item.mesh;

import iskallia.itraders.item.ItemSpawnEggTrader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class TraderEggMesh extends ItemMesh {

	public TraderEggMesh(ItemSpawnEggTrader item) {
		super(item);

		this.addMesh(this.createMesh("coin_1"));
		this.bakeMeshes();
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return this.meshes[0];
	}

}
