package kaptainwutax.itraders.entity.render;

import kaptainwutax.itraders.entity.EntityMiniGhost;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderMiniPlayer extends RenderLivingBase<EntityMiniGhost> {

    protected static IRenderFactory renderFactory = new Factory();

    private float totalTicks = 0;

    public RenderMiniPlayer(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.1f, false), 0.25f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMiniGhost entity) {
        return entity.skin.getLocationSkin();
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void renderEntityName(EntityMiniGhost entityIn, double x, double y, double z, String name, double distanceSq) {
//        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }

    @Override
    protected void preRenderCallback(EntityMiniGhost entitylivingbaseIn, float partialTickTime) {
        float size = 0.25f;
        GlStateManager.scale(size, size, size);
    }

    @Override
    public void doRender(EntityMiniGhost miniPlayer, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityPlayer parent = miniPlayer.getParent();

        if (parent == null) return;

        float oneVoxel = 1 / 16f;

        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();

        GlStateManager.color(.9f, .9f, .9f, 0.50f);
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-parent.rotationYaw, 0f, 1f, 0f);
        GlStateManager.translate(
                7 * oneVoxel,
                Math.sin(totalTicks / (2 * Math.PI)) / 20f,
                -2 * oneVoxel
        );

        super.doRender(miniPlayer, 0, 0, 0,
                entityYaw,
                partialTicks);

        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

        totalTicks += partialTicks;
    }

    public static IRenderFactory getRenderFactory() {
        return renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityMiniGhost> {

        @Override
        public Render<? super EntityMiniGhost> createRenderFor(RenderManager manager) {
            return new RenderMiniPlayer(manager);
        }

    }

}
