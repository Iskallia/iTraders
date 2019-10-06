package kaptainwutax.itraders.item;

import kaptainwutax.itraders.init.InitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class PouchInventory extends ItemStackHandler {

	public PouchInventory() {
		this.stacks = NonNullList.create();
	}
	
	public PouchInventory(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
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
    	boolean full = true;
    	
    	for (ItemStack stack : this.stacks) {
    		if(stack.isEmpty()) {
    			full = false;
    			break;
    		}
		}

    	if(full) {
    		this.expand(1);
    	}
	}
	
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