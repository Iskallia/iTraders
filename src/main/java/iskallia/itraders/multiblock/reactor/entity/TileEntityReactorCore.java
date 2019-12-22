package iskallia.itraders.multiblock.reactor.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.entity.TileEntityPowerCube;
import iskallia.itraders.capability.ModifiableEnergyStorage;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.multiblock.reactor.MultiblockReactor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class TileEntityReactorCore extends TileInventoryBase implements ITickable {

    private boolean structured = false;
    private ModifiableEnergyStorage energyStorage = generateEnergyStorage();

    public TileEntityReactorCore() {
        super(24);
    }

    @Override
    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTileFiltered(this) {
            @Override
            public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
                return existing.isEmpty() && (toAdd.getItem() == InitBlock.ITEM_POWER_CUBE);
            }
        };
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

    public ModifiableEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public boolean isStructured() {
        return structured;
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
                // Still tracing for a valid structure...
            }
        }
    }

    public void updateReactor() {
        // TODO
        Map<BlockPos, TileEntityPowerCube> powerCubes = MultiblockReactor.INSTANCE.getPowerCubes(world, pos);
        int energyToGenerate = 0;

        for (TileEntityPowerCube powerCube : powerCubes.values()) {
            if (powerCube.getRemainingTicks() > 0) {
                energyToGenerate += powerCube.getBaseRFRate() * powerCube.getRarity().getMultiplier();
            }
        }

        if (energyToGenerate > 0) {
            energyStorage.setEnergy(energyToGenerate); // TODO: Include core efficiency
            this.markForUpdate();
        }
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
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(energyStorage);

        return null;
    }

    /* --------------------------------- */

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setBoolean("Structured", structured);
        compound.setInteger("Energy", energyStorage.getEnergyStored());
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.structured = compound.getBoolean("Structured");
        this.energyStorage.setEnergy(compound.getInteger("Energy"));
    }

    /* --------------------------------- */

    @Nullable
    public static TileEntityReactorCore getReactorCore(IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityReactorCore))
            return null;

        return (TileEntityReactorCore) tileEntity;
    }

}
