package iskallia.itraders.entity.render;

import javax.annotation.Nullable;

import iskallia.itraders.entity.EntityAccelerator;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderAccelerator extends RenderLivingBase<EntityAccelerator> {

	protected static IRenderFactory renderFactory = new Factory();

	public RenderAccelerator(RenderManager renderManager) {
		super(renderManager, new ModelPlayer(0.1f, false), 0.25f);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityAccelerator entity) {
		return entity.skin.getLocationSkin();
	}

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
	}

	@Override
	protected void renderEntityName(EntityAccelerator entityIn, double x, double y, double z, String name, double distanceSq) {
		// super.renderEntityName(entityIn, x, y, z, name, distanceSq);
	}

	@Override
	protected void preRenderCallback(EntityAccelerator entitylivingbaseIn, float partialTickTime) {
		float size = 0.25f;
		GlStateManager.scale(size, size, size);
	}

	@Override
	public void doRender(EntityAccelerator entity, double x, double y, double z, float entityYaw, float partialTicks) {

		entity.limbSwingAmount = 2.0f;

		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
		GlStateManager.color(1f, 1f, 1f, 1.0f);
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(0f, 0f, 0f, 0f);

		super.doRender(entity, 0, 0, 0, entityYaw, partialTicks);

		GlStateManager.popMatrix();
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}

	public static IRenderFactory getRenderFactory() {
		return renderFactory;
	}

	public static class Factory implements IRenderFactory<EntityAccelerator> {

		@Override
		public Render<? super EntityAccelerator> createRenderFor(RenderManager manager) {
			return new RenderAccelerator(manager);
		}

	}

}
