package kaptainwutax.itraders.entity.render;

import kaptainwutax.itraders.entity.EntityMiniPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderMiniPlayer extends RenderLivingBase<EntityMiniPlayer> {

    protected static IRenderFactory renderFactory = new Factory();

    private float totalTicks = 0;

    public RenderMiniPlayer(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.1f, false), 0.25f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMiniPlayer entity) {
        return entity.skin.getLocationSkin();
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void renderEntityName(EntityMiniPlayer entityIn, double x, double y, double z, String name, double distanceSq) {
//        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }

    @Override
    protected void preRenderCallback(EntityMiniPlayer entitylivingbaseIn, float partialTickTime) {
        float size = 0.25f;
        GlStateManager.scale(size, size, size);
    }

    @Override
    public void doRender(EntityMiniPlayer miniPlayer, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityLivingBase owner = miniPlayer.owner;

        if (owner == null) return;

        float oneVoxel = 1 / 16f;

//        this.setModelVisibilities(miniPlayer);
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();

        GlStateManager.color(1f, 1f, 1f, 0.75f);
        GlStateManager.rotate(-owner.rotationYaw, 0f, 1f, 0f);
        GlStateManager.translate(
                x + 7 * oneVoxel,
                y + Math.sin(totalTicks / Math.PI) / 100f,
                z - 2 * oneVoxel
        );

        super.doRender(miniPlayer, 0, 0, 0,
                entityYaw,
                partialTicks);

        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

        totalTicks += partialTicks;
    }

    private void setModelVisibilities(EntityMiniPlayer clientPlayer) {
        ModelPlayer modelplayer = (ModelPlayer) this.getMainModel();

        modelplayer.setVisible(true);
//        modelplayer.isSneak = clientPlayer.isSneaking();
    }

    public static IRenderFactory getRenderFactory() {
        return renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityMiniPlayer> {

        @Override
        public Render<? super EntityMiniPlayer> createRenderFor(RenderManager manager) {
            return new RenderMiniPlayer(manager);
        }

    }

}
