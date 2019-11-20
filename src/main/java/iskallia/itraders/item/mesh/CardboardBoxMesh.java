package iskallia.itraders.item.mesh;

import iskallia.itraders.item.ItemCardboardBox;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class CardboardBoxMesh extends ItemMesh {

    public CardboardBoxMesh(ItemCardboardBox item) {
        super(item);

        this.addMesh(createMesh("cardboard_box_empty"));
        this.addMesh(createMesh("cardboard_box_full"));
        this.bakeMeshes();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return ItemCardboardBox.carryingTrader(stack)
                ? meshes[1] : meshes[0];
    }

}
