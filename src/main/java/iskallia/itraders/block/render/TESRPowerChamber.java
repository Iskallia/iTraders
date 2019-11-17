package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockDoublePartDirectional;
import iskallia.itraders.block.entity.TileEntityPowerChamber;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRPowerChamber extends TileEntitySpecialRenderer<TileEntityPowerChamber> {
	
	private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
	private static Entity dummyEntity;
	
	@Override
    public void render(TileEntityPowerChamber tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (!tileEntity.hasWorld())
			return;
		
		if (!tileEntity.isOccupied())
			return;
		
		if (dummyEntity == null) {
			initDummyEntity(tileEntity.getWorld());
		}
		
		IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
		
		if (!state.getValue(BlockDoublePartDirectional.PART).equals(BlockDoublePartDirectional.EnumPartType.BOTTOM)) {
			return;
		}
		
		bindTexture(tileEntity.getSkin().getLocationSkin());
		
		if (dummyEntity != null) {
			double scale = 0.075d;
			
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
			protected void entityInit() {
			}

			@Override
			protected void readEntityFromNBT(NBTTagCompound compound) {
			}

			@Override
			protected void writeEntityToNBT(NBTTagCompound compound) {
			}
		};
		
		dummyEntity.posX = 0;
		dummyEntity.posY = 0;
		dummyEntity.posZ = 0;
	}
}
