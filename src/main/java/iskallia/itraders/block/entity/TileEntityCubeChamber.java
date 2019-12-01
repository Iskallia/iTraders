package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.capability.ModifiableEnergyStorage;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemBooster;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

/*
 * Inventory Slots:
 *  0 - Input item: Only eggs
 *  1 - Booster item
 *  2 - Output item
 *
 * TODO Features:
 *  - Once started go into "Infusing" state
 *  - Stay in "Infusing" state for 5 seconds
 *  - Then by chance (modified by the Booster item as well) do one of following:
 *      * Generate & put "Power Cube" on output slot
 *      * Create & put a head item for associated subscriber on output slot
 */
public class TileEntityCubeChamber extends TileInventoryBase {

    public static final int INPUT_SLOT = 0;
    public static final int BOOSTER_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    // TODO: Extract to config <-- No rush on this tho. Can be postponed!
    public static final int CAPACITY = 10000;
    public static final int MAX_RECEIVE = 10;
    public static final int ENERGY_USAGE_PER_TICK = 10;
    public static final int REQUIRED_PROCESS_TICKS = 20;

    /* --------------------------------- */

    public CubeChamberStates state = CubeChamberStates.IDLE;
    private int remainingTicks = 0;
    private ItemStack boosterInUse = ItemStack.EMPTY;
    private ModifiableEnergyStorage energyStorage = generateEnergyStorage();
    private ItemHandlerTile outputHandler = createNewExtractionHandler();

    private ModifiableEnergyStorage generateEnergyStorage() {
        return new ModifiableEnergyStorage(CAPACITY, MAX_RECEIVE, 0) {
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

    public ItemHandlerTile getOutputHandler() {
        return outputHandler;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    /* --------------------------------- */

    public TileEntityCubeChamber() {
        super(3,// 0: input, 1: booster, 2: output
                EnumFacing.EAST, EnumFacing.WEST,
                EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.UP);
    }

    /**
     * Called by the "Infuse" button on GUI
     * in order to begin the infusion process.
     *
     * @return whether started infusion successfully or not
     */
    public boolean startPressed() {
        ItemHandlerTile inventoryHandler = this.getInventoryHandler();

        // No input, stop here
        if (inventoryHandler.getStackInSlot(INPUT_SLOT) == ItemStack.EMPTY)
            return false;

        // Output slot is occupied, cannot
        if (outputHandler.getStackInSlot(OUTPUT_SLOT) != ItemStack.EMPTY)
            return false;

        // Cannot restart an on-going infusion
        if (state == CubeChamberStates.PROCESSING)
            return false;

        // Consume booster no matter what
        ItemStack boosterStack = inventoryHandler.getStackInSlot(BOOSTER_SLOT);
        if (boosterStack != ItemStack.EMPTY) {
            this.boosterInUse = boosterStack.copy();
            boosterStack.shrink(1);
        }

        this.remainingTicks = REQUIRED_PROCESS_TICKS;
        this.state = CubeChamberStates.PROCESSING;
        this.markForUpdate();
        return true;
    }

    /**
     * Called on the SERVER-side once
     * the infusion process finished <br />
     * (A.k.a when state == PROCESSING && remainingTicks <= 0)
     */
    private void onInfusingFinished() {
        ItemHandlerTile inventoryHandler = getInventoryHandler();

        assert outputHandler.getStackInSlot(OUTPUT_SLOT).isEmpty();

        ItemStack eggStack = inventoryHandler.getStackInSlot(INPUT_SLOT);

        // Roll the dice for Head or Power Cube
        double chance = ItemBooster.getSuccessRate(boosterInUse); // TODO:
        System.out.println("Rolling on a chance of " + (chance * 100) + "%");
        if (rand.nextDouble() <= chance) {
            System.out.println("Generating Power Cube");
            ItemStack cubeStack = BlockPowerCube.generateRandomly(eggStack);
            outputHandler.setStackInSlot(OUTPUT_SLOT, cubeStack);

        } else {
            ItemStack skullStack = new ItemStack(Items.SKULL, 1, 3);
            NBTTagCompound stackNBT = new NBTTagCompound();
            stackNBT.setString("SkullOwner", eggStack.getDisplayName());
            skullStack.setTagCompound(stackNBT);
            outputHandler.setStackInSlot(OUTPUT_SLOT, skullStack);
        }

        // Consume the egg & reset booster in use
        eggStack.shrink(BOOSTER_SLOT);
        this.boosterInUse = ItemStack.EMPTY;
    }

    @Override
    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTileFiltered(this) {
            @Override
            public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
                if (state == CubeChamberStates.PROCESSING)
                    return false;

                // Slot 0 for fighter egg
                if (slot == 0 && toAdd.getItem() == InitItem.SPAWN_EGG_FIGHTER)
                    return true;

                // Slot 1 for Booster
                if (slot == 1 && toAdd.getItem() instanceof ItemBooster)
                    return true;

                return false;
            }

            @Override
            public boolean canExtractItem(int slot, int amount, @Nonnull ItemStack existing) {
                if (slot == 2)
                    return false;

                if (state == CubeChamberStates.PROCESSING)
                    return false;

                return super.canExtractItem(slot, amount, existing);
            }
        };
    }

    protected ItemHandlerTile createNewExtractionHandler() {
        return new ItemHandlerTileFiltered(this) {
            @Override
            public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot, int amount, @Nonnull ItemStack existing) {
                return (slot == 2 && state != CubeChamberStates.PROCESSING);
            }
        };
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (state == CubeChamberStates.PROCESSING) {
                if (remainingTicks <= 0) { // No ticks remaining
                    onInfusingFinished();
                    state = CubeChamberStates.IDLE;
                    markForUpdate();

                } else if (processInfusion()) { // Try infusing for this tick
                    markForUpdate();
                }
            }
        }
    }

    /**
     * Performs 1 tick of "Infusion"
     *
     * @return whether performed successfully or not
     */
    public boolean processInfusion() {
        int currentEnergy = energyStorage.getEnergyStored();

        // Does not have enough energy for this tick
        if (currentEnergy < ENERGY_USAGE_PER_TICK)
            return false;

        int extracted = energyStorage.voidEnergy(ENERGY_USAGE_PER_TICK, false);
        remainingTicks--;
        return true;
    }

    /* --------------------------------- */

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return true;

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return CapabilityEnergy.ENERGY.cast(energyStorage);

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputHandler);

        return super.getCapability(capability, facing);
    }

    /* --------------------------------- */

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.state = CubeChamberStates.values()[compound.getInteger("State")];
        this.remainingTicks = compound.getInteger("RemainingTicks");
        this.energyStorage.setEnergy(compound.getInteger("Energy"));
        this.boosterInUse = new ItemStack(compound.getCompoundTag("BoosterNBT"));

        super.readCustomNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("State", this.state.ordinal());
        compound.setInteger("RemainingTicks", this.remainingTicks);
        compound.setInteger("Energy", this.energyStorage.getEnergyStored());
        compound.setTag("BoosterNBT", this.boosterInUse.writeToNBT(new NBTTagCompound())); // TODO: Optimize (?)

        super.writeCustomNBT(compound);
    }

    public enum CubeChamberStates {
        IDLE,
        PROCESSING
    }

}
