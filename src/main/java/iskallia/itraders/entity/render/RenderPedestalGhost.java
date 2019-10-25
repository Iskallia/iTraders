package iskallia.itraders.entity.render;

import iskallia.itraders.entity.EntityMiniGhost;
import iskallia.itraders.entity.EntityPedestalGhost;
import iskallia.itraders.init.InitConfig;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderPedestalGhost extends RenderLivingBase<EntityPedestalGhost> {

    protected static IRenderFactory renderFactory = new Factory();

    private long animationStart = System.currentTimeMillis();

    public RenderPedestalGhost(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.1f, false), 0f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPedestalGhost entity) {
        return entity.skin.getLocationSkin();
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
    }

    @Override
    protected void renderEntityName(EntityPedestalGhost entityIn, double x, double y, double z, String name, double distanceSq) {
//        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }

    @Override
    protected void preRenderCallback(EntityPedestalGhost entitylivingbaseIn, float partialTickTime) {
        float size = 0.25f;
        GlStateManager.scale(size, size, size);
    }

    @Override
    public void doRender(EntityPedestalGhost pedestalGhost, double x, double y, double z, float entityYaw, float partialTicks) {
        float dt = (System.currentTimeMillis() - animationStart) / 1000f;
        float alpha = InitConfig.CONFIG_SKULL_NECKLACE.GHOST_RENDER_OPACITY;
        float oneVoxel = 1 / 16f;

        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.pushMatrix();

        GlStateManager.color(.9f, .9f, .9f, alpha);
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(.5f, 2 * oneVoxel, .5f);
        GlStateManager.rotate(getFacingAngle(pedestalGhost.facing), 0f, 1f, 0f);
        GlStateManager.translate(0, Math.sin(dt + pedestalGhost.getEntityId()) / 20f, 0);

        super.doRender(pedestalGhost, 0, 0, 0,
                entityYaw,
                partialTicks);

        GlStateManager.popMatrix();
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
    }

    protected float getFacingAngle(EnumFacing facing) {
        switch(facing) {
            case NORTH: return 0;
            case SOUTH: return 180;
            case WEST: return 90;
            case EAST: default: return -90;
        }
    }

    public static IRenderFactory getRenderFactory() {
        return renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityPedestalGhost> {

        @Override
        public Render<? super EntityPedestalGhost> createRenderFor(RenderManager manager) {
            return new RenderPedestalGhost(manager);
        }

    }

}
