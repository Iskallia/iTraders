package iskallia.itraders.item.mesh;

import iskallia.itraders.card.SubCardRarity;
import iskallia.itraders.item.ItemSubCard;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class SubCardMesh extends ItemMesh {

    public SubCardMesh(ItemSubCard item) {
        super(item);

        this.addMesh(this.createMesh("card_common"));
        this.addMesh(this.createMesh("card_rare"));
        this.addMesh(this.createMesh("card_legendary"));
        this.bakeMeshes();
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        SubCardRarity rarity = ItemSubCard.getCardRarity(stack);

        switch(rarity) {
            case RARE: return meshes[1];
            case LEGENDARY: return meshes[2];

            default: return meshes[0];
        }
    }

}
