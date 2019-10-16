package kaptainwutax.itraders.container.slot;

import javax.annotation.Nonnull;

import kaptainwutax.itraders.world.storage.PouchInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class FakeSlot extends SlotItemHandler {

	protected PouchInventory pouchInventory;

	public FakeSlot(PouchInventory inventory, int xPosition, int yPosition) {
		super(inventory, inventory.getSlots(), xPosition, yPosition);
		this.pouchInventory = inventory;
	}

	public int getIndex() {
		return PouchInventory.FAKE_SLOT;
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots
	 * as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		if (stack.isEmpty() || !pouchInventory.isItemValid(this.getIndex(), stack))
			return false;

		IItemHandler handler = this.getItemHandler();
		ItemStack remainder;
		if (handler instanceof IItemHandlerModifiable) {
			IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;
			ItemStack currentStack = handlerModifiable.getStackInSlot(this.getIndex());

			handlerModifiable.setStackInSlot(this.getIndex(), ItemStack.EMPTY);

			remainder = handlerModifiable.insertItem(this.getIndex(), stack, true);

			handlerModifiable.setStackInSlot(this.getIndex(), currentStack);
		} else {
			remainder = handler.insertItem(this.getIndex(), stack, true);
		}
		return remainder.getCount() < stack.getCount();
	}

	/**
	 * Helper fnct to get the stack in the slot.
	 */
	@Override
	@Nonnull
	public ItemStack getStack() {
		return ItemStack.EMPTY;
	}

	// Override if your IItemHandler does not implement IItemHandlerModifiable
	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack(@Nonnull ItemStack stack) {
		((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.getIndex(), stack);
		this.onSlotChanged();
	}

	/**
	 * if par2 has more items than par1, onCrafting(item,countIncrease) is called
	 */
	@Override
	public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {

	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit() {
		return this.pouchInventory.getSlotLimit(this.getIndex());
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack) {
		ItemStack maxAdd = stack.copy();
		int maxInput = stack.getMaxStackSize();
		maxAdd.setCount(maxInput);

		IItemHandler handler = this.getItemHandler();
		ItemStack currentStack = handler.getStackInSlot(this.getIndex());
		if (handler instanceof IItemHandlerModifiable) {
			IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

			handlerModifiable.setStackInSlot(this.getIndex(), ItemStack.EMPTY);

			ItemStack remainder = handlerModifiable.insertItem(this.getIndex(), maxAdd, true);

			handlerModifiable.setStackInSlot(this.getIndex(), currentStack);

			return maxInput - remainder.getCount();
		} else {
			ItemStack remainder = handler.insertItem(this.getIndex(), maxAdd, true);

			int current = currentStack.getCount();
			int added = maxInput - remainder.getCount();
			return current + added;
		}
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !this.getItemHandler().extractItem(this.getIndex(), 1, true).isEmpty();
	}

	@Override
	public IItemHandler getItemHandler() {
		return this.pouchInventory;
	}

}
