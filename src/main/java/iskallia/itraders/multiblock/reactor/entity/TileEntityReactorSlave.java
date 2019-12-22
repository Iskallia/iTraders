package iskallia.itraders.multiblock.reactor.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityReactorSlave extends TileEntitySynchronized {

    private BlockPos masterPos = null;

    public TileEntityReactorSlave() {
        super();
    }

    public boolean hasMaster() {
        return masterPos != null;
    }

    @Nullable
    public TileEntityReactorCore getMaster() {
        if (!hasMaster()) return null;

        return TileEntityReactorCore.getReactorCore(this.world, masterPos);
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    /* ----------------------------- */

    public void connectToMaster(BlockPos masterPos) {
        if (world.getTileEntity(masterPos) instanceof TileEntityReactorCore) {
            this.masterPos = masterPos;
            this.markForUpdate();
        }
    }

    public void disconnectFromMaster() {
        if (this.masterPos != null) {
            this.masterPos = null;
            this.markForUpdate();
        }
    }

    /* ----------------------------- */

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        TileEntityReactorCore reactorCore = getMaster();

        if (reactorCore == null)
            return false;

        return reactorCore.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        TileEntityReactorCore reactorCore = getMaster();

        if (reactorCore == null)
            return null;

        return reactorCore.getCapability(capability, facing);
    }

    /* ----------------------------- */

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        if (hasMaster()) {
            int x = masterPos.getX();
            int y = masterPos.getY();
            int z = masterPos.getZ();

            NBTTagCompound masterPosNBT = new NBTTagCompound();
            masterPosNBT.setInteger("x", x);
            masterPosNBT.setInteger("y", y);
            masterPosNBT.setInteger("z", z);

            compound.setTag("MasterPos", masterPosNBT);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        if (compound.hasKey("MasterPos", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound masterPosNBT = compound.getCompoundTag("MasterPos");

            int x = masterPosNBT.getInteger("x");
            int y = masterPosNBT.getInteger("y");
            int z = masterPosNBT.getInteger("z");

            this.masterPos = new BlockPos(x, y, z);
        }
    }

    /* ----------------------------- */

    @Nullable
    public static TileEntityReactorSlave getReactorSlave(IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityReactorSlave))
            return null;

        return (TileEntityReactorSlave) tileEntity;
    }

}
