package iskallia.itraders.multiblock.reactor.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import iskallia.itraders.capability.ModifiableEnergyStorage;
import iskallia.itraders.multiblock.reactor.MultiblockReactor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityReactorCore extends TileEntitySynchronized implements ITickable {

    private boolean structured = false;
    private ModifiableEnergyStorage energyStorage = generateEnergyStorage();

    public TileEntityReactorCore() {
        super();
    }

    public ModifiableEnergyStorage generateEnergyStorage() {
        return new ModifiableEnergyStorage(100_000, 0, 100) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (received != 0 && world != null)
                    markForUpdate(); // Hook dirty marking :p
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int extracted = super.extractEnergy(maxExtract, simulate);
                if (extracted != 0 && world != null)
                    markForUpdate(); // Hook dirty marking :p
                return extracted;
            }
        };
    }

    /* --------------------------------- */

    @Override
    public void update() {
        if (!world.isRemote) {
            if (structured) {
                this.updateReactor();

            } else if (MultiblockReactor.INSTANCE.matches(world, pos)) {
                MultiblockReactor.INSTANCE.structureReactor(world, pos);
                this.onReactorStructured();

            } else {
                System.out.println("Still tracing for valid Reactor structure...");
            }
        }
    }

    public void updateReactor() {
        // TODO
//        System.out.println("Updating reactor!");
    }

    public void onReactorStructured() {
        // TODO
        System.out.println("Structured Reactor!");
        this.structured = true;
        this.markForUpdate();
    }

    public void onReactorDestructed() {
        // TODO
        this.structured = false;
        this.markForUpdate();
    }

    /* --------------------------------- */

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setBoolean("Structured", structured);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.structured = compound.getBoolean("Structured");
    }

    /* --------------------------------- */

    @Nullable
    public static TileEntityReactorCore getReactorCore(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityReactorCore))
            return null;

        return (TileEntityReactorCore) tileEntity;
    }

}
