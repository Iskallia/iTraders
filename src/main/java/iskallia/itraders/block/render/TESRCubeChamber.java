package iskallia.itraders.block.render;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockCubeChamber;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TESRCubeChamber extends TileEntitySpecialRenderer<TileEntityCubeChamber> {

    public static ResourceLocation PROGRESS_BAR_TEXTURE
            = Traders.getResource("textures/blocks/cube_chamber_progress_bar.png");

    @Override
    public void render(TileEntityCubeChamber tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld())
            return;

//        if (tileEntity.state == TileEntityCubeChamber.CubeChamberStates.IDLE)
//            return;

        float infusionPercentage = tileEntity.getInfusionPercentage();

        EnumFacing facing = tileEntity.getBlockState().getValue(BlockCubeChamber.FACING);

        final float marginSide = 3 / 16f;
        final float length = 10 / 16f * infusionPercentage;
        final float marginTop = 3 / 16f;
        final float height = 3 / 16f;

        bindTexture(PROGRESS_BAR_TEXTURE);

        // TODO: Fix lighting

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GlStateManager.rotate(-facing.getHorizontalAngle() + 180, 0f, 1f, 0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f - (1 / 16f) * 0.2f);

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glTexCoord2f(length, height);
        GlStateManager.glVertex3f(1f - (marginSide + length), 1f - (marginTop + height), 0f);
        GlStateManager.glTexCoord2f(length, 0f);
        GlStateManager.glVertex3f(1f - (marginSide + length), 1f - marginTop, 0f);
        GlStateManager.glTexCoord2f(0f, 0f);
        GlStateManager.glVertex3f(1f - marginSide, 1f - marginTop, 0f);
        GlStateManager.glTexCoord2f(0f, height);
        GlStateManager.glVertex3f(1f - marginSide, 1f - (marginTop + height), 0f);

        GlStateManager.glEnd();

        GlStateManager.popMatrix();
    }
}
