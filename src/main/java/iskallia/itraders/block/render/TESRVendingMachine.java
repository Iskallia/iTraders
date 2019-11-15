package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockVendingMachine;
import iskallia.itraders.block.entity.TileEntityVendingMachine;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.util.math.MathHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class TESRVendingMachine extends TileEntitySpecialRenderer<TileEntityVendingMachine> {

    private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
    private static Entity dummyEntity;

    @Override
    public void render(TileEntityVendingMachine tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld())
            return;

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());

        if (state.getValue(BlockVendingMachine.PART) == BlockVendingMachine.EnumPartType.TOP) {
//            TileEntityVendingMachine teBottom = BlockVendingMachine.getTileEntity(
//                    tileEntity.getWorld(),
//                    tileEntity.getPos(),
//                    tileEntity.getBlockState());
//
//            if (teBottom != null) {
//                ITextComponent displayName = teBottom.getDisplayName();
//
//                if (this.rendererDispatcher.cameraHitResult != null
//                        && tileEntity.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos())) {
//                    this.setLightmapDisabled(true);
//                    this.drawNameplate(tileEntity, displayName.getFormattedText(), x, y, z, 12);
//                    this.setLightmapDisabled(false);
//                }
//            }

            return;
        }

//        super.render(tileEntity, x, y + 1, z, partialTicks, destroyStage, alpha);

        this.drawString(
                state.getValue(BlockVendingMachine.FACING).getOpposite(),
                tileEntity.getDisplayName().getUnformattedText(),
                6f / 16f, x, y, z, 0.01f);

        if (dummyEntity == null) {
            initDummyEntity(tileEntity.getWorld());
        }

        bindTexture(!tileEntity.getNickname().isEmpty()
                ? tileEntity.getSkin().getLocationSkin()
                : DefaultPlayerSkin.getDefaultSkinLegacy());

        double scale = 0.075;

        if (dummyEntity != null) {
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

    public void drawString(EnumFacing facing, String text, float yOffset, double x, double y, double z, float scale) {
        float size = (float) this.getFontRenderer().getStringWidth(text) * scale;
        float textCenter = (1.0f + size) / 2.0f;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);

        if (facing == EnumFacing.NORTH) {
            GlStateManager.translate(textCenter, yOffset, 6.0f / 16.0f - 0.4f);
            GlStateManager.rotate(180, 0, 0, 1);
        } else if (facing == EnumFacing.SOUTH) {
            GlStateManager.translate(-textCenter + 1, yOffset, (16.0f - 6.0f) / 16.0f + 0.4f);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(180, 0, 1, 0);
        } else if (facing == EnumFacing.EAST) {
            GlStateManager.translate((16.0f - 6.0f) / 16.0f + 0.4f, yOffset, textCenter);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(90, 0, 1, 0);
        } else if (facing == EnumFacing.WEST) {
            GlStateManager.translate(6.0f / 16.0f - 0.4f, yOffset, -textCenter + 1);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(270, 0, 1, 0);
        }

        GlStateManager.scale(scale, scale, scale);

        this.setLightmapDisabled(true);
        this.getFontRenderer().drawString(text, 0, 0, 0xFFFFFF);
        this.setLightmapDisabled(false);
        GlStateManager.popMatrix();
    }

    protected float getFacingAngle(IBlockState blockState) {
        return getFacingAngle(blockState.getValue(BlockHorizontal.FACING));
    }

    protected float getFacingAngle(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 0;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            case EAST:
            default:
                return -90;
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
