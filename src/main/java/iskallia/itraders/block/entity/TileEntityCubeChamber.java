package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemBooster;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

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
	
	// TODO: Extract to config <-- No rush on this tho. Can be postponed!
	public static final int CAPACITY = 10000;
	public static final int MAX_RECEIVE = 10;
	
	/* --------------------------------- */
	
	private EnergyStorage energyStorage = generateEnergyStorage();
	private int remainingTicks = 0;
	
	public CubeChamberStates state = CubeChamberStates.IDLE;
	
	private EnergyStorage generateEnergyStorage() {
		return new EnergyStorage(CAPACITY, MAX_RECEIVE, 0);
	}
	
	public EnergyStorage getEnergyStorage() {
		return energyStorage;
	}
	
	/* --------------------------------- */
	
	public TileEntityCubeChamber() {
		super(3); // 0: input, 1: booster, 2: output
	}
	
	public boolean startPressed() {
		// Start if it has something in slot 0
		if (
				this.getInventoryHandler().getStackInSlot(0) != ItemStack.EMPTY
				&& this.state != CubeChamberStates.PROCESSING) {
			// If there's a booster item, consume it
			if (this.getInventoryHandler().getStackInSlot(1) != ItemStack.EMPTY) {
				// TODO: Save the booster item for later
				this.getInventoryHandler().getStackInSlot(1).shrink(1);
			}
			
			this.remainingTicks = 100;
			this.state = CubeChamberStates.PROCESSING;
			return true;
		}
		
		return false; // <-- Returns if it successfully started the process
	}
	
	private void onInfusingFinished() {
		// This method will be called once the "infusion" process is finished
		
		ItemStack eggStack = getInventoryHandler().getStackInSlot(0);
		// Get it from the consumed one?
		ItemStack boosterStack = getInventoryHandler().getStackInSlot(1);
		
		// Consume the egg
		eggStack.shrink(1);
		
		double successChance = ItemBooster.getSuccessRate(boosterStack);
		
		//See if it will yield a Power Cube or Head
		if (successChance > rand.nextDouble()) {
			// Yields a Power Cube
		} else {
			// Yields a Head
		}
		
		// TODO: Put the result on output slot (inventory[2])
		
		this.state = CubeChamberStates.FINISHED;
	}
	
	@Override
	protected ItemHandlerTile createNewItemHandler() {
		// (?) You can see TileEntityCryoChamber::createNewItemHandler for reference! ^^
		return new ItemHandlerTileFiltered(this) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				// If it's not the right item for the right slot, return the stack
				if (!this.canInsertItem(slot, stack, stack))
					return stack;
				
				return super.insertItem(slot, stack, simulate);
			}
			
			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				if (!this.canExtractItem(slot, amount, this.getStackInSlot(slot)))
					return ItemStack.EMPTY;
				
				return this.getStackInSlot(slot);
			}
			
			@Override
			public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
				// Slot 0 for fighter egg
				if (slot == 0 && existing.getItem() == InitItem.SPAWN_EGG_FIGHTER)
					return true;
				
				// Slot 1 for Booster
				if (slot == 1 && existing.getItem() instanceof ItemBooster)
					return true;
				
				return false;
			}
			
			@Override
			public boolean canExtractItem(int slot, int amount, @Nonnull ItemStack existing) {
				if (slot != 2 || this.getStackInSlot(2) == ItemStack.EMPTY)
					return false;
				
				return super.canExtractItem(slot, amount, existing);
			}
		};
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (this.remainingTicks > 0)
				--remainingTicks;
			
			// Once the "Infusing" process is done, callback onInfusingFinished()
			if (this.state == CubeChamberStates.PROCESSING && this.remainingTicks == 0) {
				onInfusingFinished();
			}
			
			this.markForUpdate();
		}
	}
	
	/* --------------------------------- */
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		
		return super.hasCapability(capability, facing);
	}
	
	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return CapabilityEnergy.ENERGY.cast(energyStorage);

		return super.getCapability(capability, facing);
	}
	
	/* --------------------------------- */
	
	@Override
	public void readCustomNBT(NBTTagCompound compound) {
		// Set saved energy in the energy storage, is ther a best way?
		int savedEnergy = compound.getInteger("Energy");
		
		while (this.energyStorage.getEnergyStored() < savedEnergy) {
			this.energyStorage.receiveEnergy(savedEnergy, false);
		}
		
		this.remainingTicks = compound.getInteger("RemainingTicks");
		
		super.readCustomNBT(compound);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound compound) {
		compound.setInteger("Energy", this.energyStorage.getEnergyStored());
		compound.setInteger("RemainingTicks", this.remainingTicks);
		
		super.writeCustomNBT(compound);
	}
	
	public enum CubeChamberStates {
		IDLE, 
		PROCESSING,
		FINISHED
	};
}
