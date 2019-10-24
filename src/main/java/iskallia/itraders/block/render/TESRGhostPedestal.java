package iskallia.itraders.block.render;

import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TESRGhostPedestal extends TileEntitySpecialRenderer<TileEntityGhostPedestal> {

    @Override
    public void render(TileEntityGhostPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }

}
