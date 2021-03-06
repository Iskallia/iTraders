package iskallia.itraders.block.render;

import iskallia.itraders.block.entity.TileEntityCryoChamber;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.util.math.MathHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TESRCryoChamber extends TileEntitySpecialRenderer<TileEntityCryoChamber> {

    private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
    private static Entity dummyEntity;

    private long animationStart = System.currentTimeMillis();

    @Override
    public void render(TileEntityCryoChamber tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld())
            return;

        if (dummyEntity == null) {
            initDummyEntity(tileEntity.getWorld());
        }

        if(tileEntity.state == TileEntityCryoChamber.CryoState.EMPTY)
            return; // Nothing to render

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());

        if (tileEntity.state == TileEntityCryoChamber.CryoState.SHRINKING) {
            bindTexture(!tileEntity.getNickname().isEmpty()
                    ? tileEntity.getSkin().getLocationSkin()
                    : DefaultPlayerSkin.getDefaultSkinLegacy());

            if (dummyEntity != null) {
                double scale = MathHelper.map(tileEntity.shrinkingRemainingTicks,
                        InitConfig.CONFIG_CRYO_CHAMBER.SHRINKING_TICKS, 0,
                        0.075f, 0.010f);

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5f, y + 1f, z + 0.5f);
                GlStateManager.rotate(getFacingAngle(state), 0f, 1f, 0f);
                GlStateManager.rotate(180f, 0f, 0f, 1f);
                GlStateManager.rotate(180f, 0f, 1f, 0f);
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.translate(0, -scale * 175, 0);
                playerModel.render(dummyEntity, 0, 0, 0, 0, 0, 1f);
                GlStateManager.popMatrix();
            }
        }

        if(tileEntity.state == TileEntityCryoChamber.CryoState.DONE) {
            double dt = (System.currentTimeMillis() - animationStart) / 1000d;
            double animOffsetY = Math.sin(2 * dt + tileEntity.hashCode() % 100) / 16d;
            float animRotation = (float) (dt * 50d);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5d, y + 0.75f + animOffsetY, z + 0.5d);
            GlStateManager.rotate(getFacingAngle(state) + animRotation, 0f, 1f, 0f);
            Minecraft.getMinecraft().getRenderItem().renderItem(
                    tileEntity.getContent(),
                    ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

    protected float getFacingAngle(IBlockState blockState) {
        return getFacingAngle(blockState.getValue(BlockHorizontal.FACING));
    }

    protected float getFacingAngle(EnumFacing facing) {
        switch(facing) {
            case NORTH: return 0;
            case SOUTH: return 180;
            case WEST: return 90;
            case EAST: default: return -90;
        }
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
