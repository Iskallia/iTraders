package iskallia.itraders.entity.render;

import javax.annotation.Nullable;

import iskallia.itraders.entity.EntityMiniGhost;
import iskallia.itraders.init.InitConfig;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMiniGhost extends RenderLivingBase<EntityMiniGhost> {

    protected static IRenderFactory renderFactory = new Factory();

    private long animationStart = System.currentTimeMillis();

    public RenderMiniGhost(RenderManager renderManager) {
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
    public void doRender(EntityMiniGhost miniGhost, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityPlayer parent = miniGhost.getParent();

        if (parent == null) return;

        float oneVoxel = 1 / 16f;
        float alpha = InitConfig.CONFIG_SKULL_NECKLACE.GHOST_RENDER_OPACITY;
        float dt = (System.currentTimeMillis() - animationStart) / 1000f;

        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();

        GlStateManager.color(.9f, .9f, .9f, alpha);
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-parent.rotationYaw, 0f, 1f, 0f);
        GlStateManager.translate(
                7 * oneVoxel,
                Math.sin(dt + miniGhost.getEntityId()) / 20f,
                -2 * oneVoxel
        );

        super.doRender(miniGhost, 0, 0, 0,
                entityYaw,
                partialTicks);

        GlStateManager.popMatrix();
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
    }

    public static IRenderFactory getRenderFactory() {
        return renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityMiniGhost> {

        @Override
        public Render<? super EntityMiniGhost> createRenderFor(RenderManager manager) {
            return new RenderMiniGhost(manager);
        }

    }

}
