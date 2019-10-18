package kaptainwutax.itraders.block.entity;

import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.texture.TextureSpritePlane;
import hellfirepvp.astralsorcery.client.util.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import kaptainwutax.itraders.block.BlockInfusionCauldron;
import kaptainwutax.itraders.client.SpriteHelper;
import kaptainwutax.itraders.init.InitBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityInfusionCauldron extends TileEntity implements ITickable {

	public static final int MAX_FLUID = 1000;

	private FluidTank tank = new FluidTank(MAX_FLUID) {

		@Override
		protected void onContentsChanged() {

			// handle the blockstate (water level)
			int level = getLevelFromTank(this.getFluidAmount());

			IBlockState state = world.getBlockState(pos);
			// taken from BlockCauldron#setWaterLevel()
			world.setBlockState(pos, state.withProperty(BlockInfusionCauldron.LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);

			world.updateComparatorOutputLevel(pos, InitBlock.INFUSION_CAULDRON);

			IBlockState newState = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, newState, 3);
			markDirty();

		}

		private int getLevelFromTank(int currentWaterLevel) {
			if (currentWaterLevel == 1000)
				return 3;
			else if (currentWaterLevel > 350)
				return 2;
			else if (currentWaterLevel > 0)
				return 1;
			else
				return 0;
		}
	};

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public FluidTank getTank() {
		return tank;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tankNBT = new NBTTagCompound();
		tank.writeToNBT(tankNBT);
		compound.setTag("tank", tankNBT);

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		if (compound.hasKey("tank")) {
			tank.readFromNBT(compound.getCompoundTag("tank"));
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		}
		return super.getCapability(capability, facing);
	}
	

    private Object effectRef;

    @Override
    public void update() {
        if (world.isRemote) {
            playRitualEffect();
        }
    }

    @SideOnly(Side.CLIENT)
    private void playRitualEffect() {
        TextureSpritePlane spr = (TextureSpritePlane) effectRef;
        if (spr == null || spr.canRemove() || spr.isRemoved()) {
            SpriteSheetResource sprite = SpriteHelper.loadEffectSheet("halo_cauldron", 6, 8);
            spr = EffectHandler.getInstance().textureSpritePlane(sprite, Vector3.RotAxis.Y_AXIS.clone());
            spr.setPosition(new Vector3(this).add(0.5, 0.06, 0.5));
            spr.setAlphaOverDistance(false);
            spr.setNoRotation(45);
            spr.setRefreshFunc(() -> {
                if (isInvalid()) {
                    return false;
                }
                if (this.getWorld().provider == null || Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().world.provider == null) {
                    return false;
                }
                return this.getWorld().provider.getDimension() == Minecraft.getMinecraft().world.provider.getDimension();
            });
            spr.setScale(3F);
            effectRef = spr;
        }
    }

}
