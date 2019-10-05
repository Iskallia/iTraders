package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class EggPouchMesh implements ItemMeshDefinition {

    public static final ModelResourceLocation MODEL_RESOURCE_LOCATION = new ModelResourceLocation(
            Traders.MOD_ID + ":egg_pouch",
            "inventory"
    );

    public EggPouchMesh(Item item) {
        ModelBakery.registerItemVariants(item, MODEL_RESOURCE_LOCATION);
    }

    @Override
    @Nonnull
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        return MODEL_RESOURCE_LOCATION;
    }

}
