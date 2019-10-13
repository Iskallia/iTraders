package kaptainwutax.itraders.item.mesh;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FighterEggMesh implements ItemMeshDefinition {
	
	public static ModelResourceLocation[] TEXTURES = new ModelResourceLocation[5];
	public static ModelResourceLocation DEFAULT = null;
	
	/*
	ItemStack:
	{
		display: {
			Name: whatever
		},
		EntityTag: {
			SizeMultiplier: 2.0f
			SubData: {
				Months: 6
				Time: 1234L
			}
		}
	}
	 **/
	
	public FighterEggMesh(ItemSpawnEggFighter item) {
		for(int i = 0; i < 5; i++) {
			TEXTURES[i] = new ModelResourceLocation(
					Traders.MOD_ID + ":" + "badge_" + (i + 1),
					"inventory"
				);
		}
		
		DEFAULT = TEXTURES[0];
		ModelBakery.registerItemVariants(InitItem.SPAWN_EGG_FIGHTER, TEXTURES);
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {		
		if(!stack.hasTagCompound())return DEFAULT;
		NBTTagCompound stackTag = stack.getTagCompound();

		if(!stackTag.hasKey("EntityTag"))return DEFAULT;
		NBTTagCompound entityTag = stackTag.getCompoundTag("EntityTag");

		if(!entityTag.hasKey("SubData"))return DEFAULT;
		NBTTagCompound subTag = entityTag.getCompoundTag("SubData");

		if(!subTag.hasKey("Months"))return DEFAULT;
		int months = subTag.getInteger("Months");

		if(months < 3) {
			return TEXTURES[0];
		} else if(months < 6) {
			return TEXTURES[1];
		} else if(months < 12) {
			return TEXTURES[2];
		} else if(months < 24) {
			return TEXTURES[3];
		} else {
			return TEXTURES[4];
		}
	}

}
