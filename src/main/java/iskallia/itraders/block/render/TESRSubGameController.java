package iskallia.itraders.block.render;

import iskallia.itraders.block.entity.TileEntitySubGameController;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TESRSubGameController extends TileEntitySpecialRenderer<TileEntitySubGameController> {

    @Override
    public void render(TileEntitySubGameController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        te.getController().renderTESRLaneContents(te, x, y, z, partialTicks);
    }
}
