package kaptainwutax.itraders.entity.render;

import kaptainwutax.itraders.entity.EntityTrader;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTrader extends RenderLivingBase<EntityTrader> {

	protected static IRenderFactory renderFactory = new Factory();
	
    private final boolean smallArms;

    public RenderTrader(RenderManager renderManager) {
        this(renderManager, false);
    }

    public RenderTrader(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.6F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.smallArms = useSmallArms;
    }

    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }

    public void doRender(EntityTrader entity, double x, double y, double z, float entityYaw, float partialTicks) {  	
    	double d0 = y;

        if (entity.isSneaking()) {
            d0 = y - 0.125D;
        }

        this.setModelVisibilities(entity);
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        super.doRender(entity, x, d0, z, entityYaw, partialTicks);
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
    }

    private void setModelVisibilities(EntityTrader clientPlayer) {
        ModelPlayer modelplayer = this.getMainModel();

        ItemStack itemstack = clientPlayer.getHeldItemMainhand();
        ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
        modelplayer.setVisible(true);
        modelplayer.isSneak = clientPlayer.isSneaking();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

        if(!itemstack.isEmpty()) {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if(clientPlayer.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK) {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW) {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if(!itemstack1.isEmpty()) {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if(clientPlayer.getItemInUseCount() > 0) {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction1 == EnumAction.BOW) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if(clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
            modelplayer.rightArmPose = modelbiped$armpose;
            modelplayer.leftArmPose = modelbiped$armpose1;
        } else {
            modelplayer.rightArmPose = modelbiped$armpose1;
            modelplayer.leftArmPose = modelbiped$armpose;
        }
    }

    public ResourceLocation getEntityTexture(EntityTrader entity) {
        return entity.skin.getLocationSkin();
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(EntityTrader entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;
        GlStateManager.scale(f, f, f);
    }

    protected void renderEntityName(EntityTrader entityIn, double x, double y, double z, String name, double distanceSq) {
        if(distanceSq < 100.0D) {
            Scoreboard scoreboard = entityIn.world.getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

            if(scoreobjective != null) {
                Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
                this.renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.025F);
            }
        }

        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }

    public void renderRightArm(EntityTrader clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.bipedRightArm.rotateAngleX = 0.0F;
        modelplayer.bipedRightArm.render(0.0625F);
        modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedRightArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    public void renderLeftArm(EntityTrader clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0F;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArm.render(0.0625F);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    protected void renderLivingAt(EntityTrader entityLivingBaseIn, double x, double y, double z) {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        } else {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }

    protected void applyRotations(EntityTrader entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if(entityLiving.isElytraFlying()) {
            super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
            float f = (float)entityLiving.getTicksElytraFlying() + partialTicks;
            float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
            GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
            Vec3d vec3d = entityLiving.getLook(partialTicks);
            double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

            if(d0 > 0.0D && d1 > 0.0D) {
                double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
                GlStateManager.rotate((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
            }
        } else {
            super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
        }
    }
	
	public static IRenderFactory getRenderFactory() {
		return RenderTrader.renderFactory;
	}
	
	public static class Factory implements IRenderFactory<EntityTrader> {
		@Override
		public Render<EntityTrader> createRenderFor(RenderManager manager) {
			return new RenderTrader(manager);
		}		
	}
	
}
