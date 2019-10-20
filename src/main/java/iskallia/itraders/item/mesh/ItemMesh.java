package iskallia.itraders.item.mesh;

import java.util.ArrayList;
import java.util.List;

import iskallia.itraders.Traders;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemMesh implements ItemMeshDefinition {

	protected Item item;
	protected ModelResourceLocation[] meshes;

	private List<ModelResourceLocation> meshesList = new ArrayList<>();

	public ItemMesh(Item item) {
		this.item = item;
	}

	public ModelResourceLocation createMesh(String name) {
		return new ModelResourceLocation(Traders.getResource(name).toString(), "inventory");
	}

	public void addMesh(ModelResourceLocation mesh) {
		this.meshesList.add(mesh);
	}

	public void bakeMeshes() {
		this.meshes = this.meshesList.toArray(new ModelResourceLocation[this.meshesList.size()]);
		ModelBakery.registerItemVariants(item, meshes);
	}

	@Override
	public abstract ModelResourceLocation getModelLocation(ItemStack stack);

}
