package iskallia.itraders.item.mesh;

import iskallia.itraders.item.ItemSpawnEggFighter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FighterEggMesh extends ItemMesh {

	public FighterEggMesh(ItemSpawnEggFighter item) {
		super(item);

		this.addMesh(this.createMesh("badge_1"));
		this.addMesh(this.createMesh("badge_2"));
		this.addMesh(this.createMesh("badge_3"));
		this.addMesh(this.createMesh("badge_4"));
		this.addMesh(this.createMesh("badge_5"));
		this.bakeMeshes();
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		if (!stack.hasTagCompound())
			return this.meshes[0];
		NBTTagCompound stackTag = stack.getTagCompound();

		if (!stackTag.hasKey("EntityTag"))
			return this.meshes[0];
		NBTTagCompound entityTag = stackTag.getCompoundTag("EntityTag");

		if (!entityTag.hasKey("SubData"))
			return this.meshes[0];
		NBTTagCompound subTag = entityTag.getCompoundTag("SubData");

		if (!subTag.hasKey("Months"))
			return this.meshes[0];
		int months = subTag.getInteger("Months");

		if (months < 3) {
			return this.meshes[0];
		} else if (months < 6) {
			return this.meshes[1];
		} else if (months < 12) {
			return this.meshes[2];
		} else if (months < 24) {
			return this.meshes[3];
		} else {
			return this.meshes[4];
		}
	}

}
