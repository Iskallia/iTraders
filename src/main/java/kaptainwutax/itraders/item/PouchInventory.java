package kaptainwutax.itraders.item;

import javax.annotation.Nonnull;

import org.omg.CORBA.PRIVATE_MEMBER;

import kaptainwutax.itraders.init.InitItem;
import net.minecraft.client.renderer.texture.Stitcher.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import scala.reflect.internal.Trees.This;

public class PouchInventory extends ItemStackHandler {

	public PouchInventory() {
		this.stacks = NonNullList.create();
	}
	
	public PouchInventory(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
	}

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    	this.expandToSlot(slot);
    	super.setStackInSlot(slot, stack);
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
    	this.expandToSlot(slot);
    	return super.getStackInSlot(slot);
    }
	
	public void expand(int rows) {
		for(int i = 0; i < rows * 9; i++) {
			this.stacks.add(ItemStack.EMPTY);
		}
	}
	
	public void expandToSlot(int slot) {
		if(this.stacks.size() <= slot)System.out.println("To " + slot);
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
	
    protected void onContentsChanged(int slot) {  
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

	public void purge() {
		for(int slot = this.stacks.size() - 1; slot >= 0; slot--) {
			if(!this.getStackInSlot(slot).isEmpty())break;
			else this.stacks.remove(slot);
		}
	}
	
}