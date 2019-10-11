package kaptainwutax.itraders.item;

import kaptainwutax.itraders.init.InitItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PouchInventory extends ItemStackHandler {

	public PouchInventory() {
		this.stacks = NonNullList.create();
	}
	
	public PouchInventory(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
	}

	public List<Integer> filterIndices(String searchQuery) {
		List<Integer> filtered = new LinkedList<>();

		for (int i = 0; i < this.stacks.size(); i++) {
			ItemStack itemStack = this.stacks.get(i);
			if(itemStack == ItemStack.EMPTY) continue;
			if(itemStack.getDisplayName().toLowerCase().contains(searchQuery.toLowerCase()))
				filtered.add(i);
		}

		return filtered;
	}

	public boolean isFull() {
		for (ItemStack stack : this.stacks) {
			if(stack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public ItemStack randomEgg() {
		List<Integer> nonEmptySlots = new LinkedList<>();

		for (int i = 0; i < this.stacks.size(); i++) {
			if(!this.stacks.get(i).isEmpty()) {
				nonEmptySlots.add(i);
			}
		}

		if(nonEmptySlots.size() == 0)
			return null;

		// Pick a random non-empty slot
		int index = nonEmptySlots.get(new Random().nextInt(nonEmptySlots.size()));
		ItemStack itemStack = this.stacks.get(index);

		// Remove from the inventory
		this.stacks.set(index, ItemStack.EMPTY);

		// Pass extracted item stack
		return itemStack;
	}

	public boolean putStackOnFirstEmpty(ItemStack stack) {
		boolean expanded = false;

		if(isFull()) {
			expand(1);
			expanded = true;
		}

		for (int i = 0; i < this.stacks.size(); i++) {
			if(this.stacks.get(i) == ItemStack.EMPTY) {
				this.stacks.set(i, stack.copy());
				break;
			}
		}

		return expanded;
	}

	@Override
	public void setSize(int size) {
		this.stacks = NonNullList.create();
		this.expandToSlot(size - 1);
	}
	
	public void expand(int rows) {
		for(int i = 0; i < rows * 9; i++) {
			this.stacks.add(ItemStack.EMPTY);
		}
	}
	
	@Override
	protected void validateSlotIndex(int slot) {
		this.expandToSlot(slot);
		super.validateSlotIndex(slot);
	}
	
	public void expandToSlot(int slot) {
		while(this.stacks.size() <= slot) {
			this.stacks.add(ItemStack.EMPTY);
		}
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if(stack.getItem() != InitItem.SPAWN_EGG_FIGHTER) {
			return false;
		}
	
		return super.isItemValid(slot, stack);
	}
	
	public void rescaleToFit() {
    	if(isFull()) {
    		this.expand(1);
    	}
	}

	@Override
    protected void onContentsChanged(int slot) {  
    	this.rescaleToFit();
    }
	
    public void purge() {
		for(int slot = this.stacks.size() - 1; slot >= 0; slot--) {
			if(!this.getStackInSlot(slot).isEmpty())break;
			else this.stacks.remove(slot);
		}		
    }
    
}