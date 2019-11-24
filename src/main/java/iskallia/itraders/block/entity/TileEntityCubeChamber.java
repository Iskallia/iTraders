package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
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
	public static final int CAPACITY = 1000;
	public static final int MAX_RECEIVE = 10;
	
	/* --------------------------------- */
	
	private EnergyStorage energyStorage = generateEnergyStorage();
	// TODO: add all your state variables here.
	
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
		// This method will be called from GUI button!
		// TODO: attempt to start the cube generating process
		// TODO: Consume 1 Booster once successfully started, but store the used booster
		// for later!
		return false; // <-- Returns if it successfully started the process
	}
	
	private void onInfusingFinished() {
		// This method will be called once the "infusion" process is finished
		
		ItemStack eggStack = getInventoryHandler().getStackInSlot(0);
		ItemStack boosterStack = getInventoryHandler().getStackInSlot(1);
		
		double successChance = ItemBooster.getSuccessRate(boosterStack);
		
		// TODO: See if it will yield a Power Cube or Head
		// TODO: Put the result on output slot (inventory[2])
	}
	
	@Override
	protected ItemHandlerTile createNewItemHandler() {
		// (?) You can see TileEntityCryoChamber::createNewItemHandler for reference! ^^
		return new ItemHandlerTileFiltered(this) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				// TODO: input item to proper slot. Remember, index 0 for input, index 1 for
				// booster! :D
				return super.insertItem(slot, stack, simulate);
			}
			
			@Nonnull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				// TODO: extract output slot (inventory[2]) if not empty
				return super.extractItem(slot, amount, simulate);
			}
			
			@Override
			public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
				// TODO: return true if a fighter egg OR a booster item
				return super.canInsertItem(slot, toAdd, existing);
			}
			
			@Override
			public boolean canExtractItem(int slot, int amount, @Nonnull ItemStack existing) {
				// TODO: return true if only and only inventory[2] is not empty
				return super.canExtractItem(slot, amount, existing);
			}
		};
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			// TODO: Your whole server tick update logic
			
			// TODO: Once the "Infusing" process is done, callback onInfusingFinished()
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
		// TODO: read the NBT and deserialize them into the fields you have here!
		
		// Set saved energy in the energy storage How?
		
		super.readCustomNBT(compound);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound compound) {
		// TODO: write all of the current state with the fields you have here!
		
		compound.setInteger("Energy", this.energyStorage.getEnergyStored());
		
		super.writeCustomNBT(compound);
	}
}
