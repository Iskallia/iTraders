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
        int remainingDamage = stack.getMaxDamage() - stack.getItemDamage();
        return remainingDamage > 1 ? meshes[0] : meshes[1];
    }

}
