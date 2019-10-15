package kaptainwutax.itraders.item.mesh;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MinerEggMesh extends ItemMesh {

	public MinerEggMesh(Item item) {
		super(item);
		
		this.addMesh(this.createMesh("miner_coin_1"));
		this.bakeMeshes();
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return this.meshes[0];
	}


}
