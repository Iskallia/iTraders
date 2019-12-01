package iskallia.itraders.block.render;

import iskallia.itraders.block.entity.TileEntityPowerCube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TESRPowerCube extends TileEntitySpecialRenderer<TileEntityPowerCube> {

    private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
    private static Entity dummyEntity;

    @Override
    public void render(TileEntityPowerCube tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld())
            return;

        if (tileEntity.getNickname().isEmpty())
            return;

        if (dummyEntity == null) {
            initDummyEntity(tileEntity.getWorld());
        }

        bindTexture(!tileEntity.getNickname().isEmpty()
                ? tileEntity.getSkin().getLocationSkin()
                : DefaultPlayerSkin.getDefaultSkinLegacy());

        double scale = 0.02;

        if (dummyEntity != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5f, y + 1f, z + 0.5f);
            GlStateManager.rotate(-tileEntity.getFighterRotation(), 0f, 1f, 0f);
            GlStateManager.rotate(180f, 0f, 0f, 1f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0, -scale * 125 + 20, 0);
            playerModel.bipedHead.render(1f);
            playerModel.bipedBody.render(1f);
            playerModel.bipedLeftLeg.render(1.02f);
            playerModel.bipedRightLeg.render(1.02f);
            playerModel.bipedLeftArm.render(1.05f);
            playerModel.bipedRightArm.render(1.05f);

            playerModel.bipedBodyWear.render(1f);
            playerModel.bipedHeadwear.render(1f);
            playerModel.bipedLeftLegwear.render(1.02f);
            playerModel.bipedRightLegwear.render(1.02f);
            playerModel.bipedLeftArmwear.render(1.05f);
            GlStateManager.translate(0f, 0f, -11.05f); // No idea why right arm-wear isn't orienting correctly :/
            playerModel.bipedRightArmwear.render(1.05f);
            GlStateManager.popMatrix();
        }

        super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
    }

    private void initDummyEntity(World world) {
        dummyEntity = new Entity(world) {
            @Override
            protected void entityInit() {}

            @Override
            protected void readEntityFromNBT(NBTTagCompound compound) {}

            @Override
            protected void writeEntityToNBT(NBTTagCompound compound) {}
        };
        dummyEntity.posX = 0;
        dummyEntity.posY = 0;
        dummyEntity.posZ = 0;
    }

}
