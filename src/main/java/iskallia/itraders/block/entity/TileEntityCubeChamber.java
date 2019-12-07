package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.capability.ModifiableEnergyStorage;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemBooster;
import iskallia.itraders.util.math.Randomizer;
import iskallia.itraders.util.profile.SkinProfile;
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
 */
public class TileEntityCubeChamber extends TileInventoryBase {

    public static final int INPUT_SLOT = 0;
    public static final int BOOSTER_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    /* --------------------------------- */

    @Nonnull
    @SideOnly(Side.CLIENT)
    public SkinProfile skin = new SkinProfile();

    @SideOnly(Side.CLIENT)
    public long animationStartTime = Randomizer.randomInt();

    private boolean receivedRedstoneSignal = false;

    public CubeChamberStates state = CubeChamberStates.IDLE;
    private int remainingTicks = 0;
    private ItemStack boosterInUse = ItemStack.EMPTY;
    private ModifiableEnergyStorage energyStorage = generateEnergyStorage();
    private ItemHandlerTile outputHandler = createNewExtractionHandler();

    private ModifiableEnergyStorage generateEnergyStorage() {
        return new ModifiableEnergyStorage(
                InitConfig.CONFIG_CUBE_CHAMBER.CAPACITY,
                InitConfig.CONFIG_CUBE_CHAMBER.ENERGY_MAX_INPUT, 0) {
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

    public boolean getReceivedRedstoneSignal() {
        return receivedRedstoneSignal;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public float getInfusionPercentage() {
        float maxTicks = InitConfig.CONFIG_CUBE_CHAMBER.INFUSION_TICKS;
        float currentTicks = maxTicks - remainingTicks;
        return currentTicks / maxTicks;
    }

    /* --------------------------------- */

    public TileEntityCubeChamber() {
        super(3,// 0: input, 1: booster, 2: output
                EnumFacing.EAST, EnumFacing.WEST,
                EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.UP);
    }

    public boolean canStartInfusion() {
        if (state == TileEntityCubeChamber.CubeChamberStates.PROCESSING)
            return false;

        ItemStack eggStack = getInventoryHandler().getStackInSlot(INPUT_SLOT);
        ItemStack outputStack = getOutputHandler().getStackInSlot(OUTPUT_SLOT);

        return !eggStack.isEmpty() && outputStack.isEmpty();
    }

    public void onRedstoneSignalChanged() {
        receivedRedstoneSignal = !receivedRedstoneSignal;
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

        this.remainingTicks = InitConfig.CONFIG_CUBE_CHAMBER.INFUSION_TICKS;
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
        if (rand.nextDouble() <= chance) {
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
        int energyRequired = InitConfig.CONFIG_CUBE_CHAMBER.INFUSION_ENERGY_CONSUMPTION_PER_TICK;

        // Does not have enough energy for this tick
        if (currentEnergy < energyRequired)
            return false;

        int extracted = energyStorage.voidEnergy(energyRequired, false);
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
        this.receivedRedstoneSignal = compound.getBoolean("SignalReceived");

        super.readCustomNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setInteger("State", this.state.ordinal());
        compound.setInteger("RemainingTicks", this.remainingTicks);
        compound.setInteger("Energy", this.energyStorage.getEnergyStored());
        compound.setTag("BoosterNBT", this.boosterInUse.writeToNBT(new NBTTagCompound())); // TODO: Optimize (?)
        compound.setBoolean("SignalReceived", this.receivedRedstoneSignal);

        super.writeCustomNBT(compound);
    }

    public enum CubeChamberStates {
        IDLE,
        PROCESSING
    }

}
