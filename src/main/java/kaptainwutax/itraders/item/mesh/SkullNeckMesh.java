package kaptainwutax.itraders.item.mesh;

import kaptainwutax.itraders.item.ItemSkullNeck;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class SkullNeckMesh extends ItemMesh {

    public SkullNeckMesh(ItemSkullNeck item) {
        super(item);

        this.addMesh(createMesh("skull_neck"));
        this.addMesh(createMesh("skull_neck_drained"));
        this.bakeMeshes();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return ((ItemSkullNeck) item).getMagicPower(stack) > 0
                ? meshes[0] : meshes[1];
    }

}
