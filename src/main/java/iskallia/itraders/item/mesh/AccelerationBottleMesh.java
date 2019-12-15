package iskallia.itraders.item.mesh;

import iskallia.itraders.item.ItemAccelerationBottle;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AccelerationBottleMesh extends ItemMesh {

    public AccelerationBottleMesh(ItemAccelerationBottle item) {
        super(item);

        this.addMesh(createMesh("sub_soul_essence_empty"));
        this.addMesh(createMesh("sub_soul_essence_full"));
        this.bakeMeshes();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        Item item = stack.getItem();

        if (!(item instanceof ItemAccelerationBottle)) {
            return null;
        }

        ItemAccelerationBottle accelerationBottle = (ItemAccelerationBottle) item;

        return accelerationBottle.isBottleEmpty(stack) ? meshes[0] : meshes[1];
    }

}
