package kaptainwutax.itraders.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import kaptainwutax.itraders.init.InitItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class PouchInventory implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

	public static final int FAKE_SLOT = -1;
	protected List<ItemStack> inventoryStacks = new ArrayList<>();
	protected List<ItemStack> fakeInventoryStacks = new ArrayList<>();
	public int currentScroll = 1;
	public int totalScroll = 1;
	protected String searchQuery = "";
	
	public Map<Integer, Integer> slotOffsets = new HashMap<>();
	private List<Container> listeners = new ArrayList<>();
	
	public PouchInventory() {
		
	}

	public PouchInventory(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
	}
	
	public void addListener(Container container) {
		this.listeners.add(container);
	}
	
	public void removeListener(Container container) {
		this.listeners.remove(container);
		if(this.listeners.isEmpty())this.setSearchQuery("");
	}
	
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
		this.onContentsChanged();
	}
	
	public void move(int amount) {
		this.currentScroll += amount;
		this.totalScroll = MathHelper.ceil((float)this.inventoryStacks.size() / 9.0f) - 5;
		if(this.totalScroll < 1)this.totalScroll = 1;
		if(this.currentScroll < 1)this.currentScroll = 1;
		if(this.currentScroll > this.totalScroll)this.currentScroll = this.totalScroll;
		this.onContentsChanged();
	}

	private void updateFakeInventory() {
		this.fakeInventoryStacks.clear();
		this.slotOffsets.clear();
		
		if(this.searchQuery.isEmpty()) {
			int min = (this.currentScroll - 1) * 9;
			
			for(int realIndex = min; realIndex < min + 6 * 9; realIndex++) {				
				if(!this.slotExists(realIndex)) {
					this.fakeInventoryStacks.add(ItemStack.EMPTY);
				} else {
					this.fakeInventoryStacks.add(this.inventoryStacks.get(realIndex));					
				}
				
				this.slotOffsets.put(realIndex - min, realIndex);
			}
		} else {
			int realIndex = 0;
			int currentIndex = 0;
			
			for(ItemStack stack: this.inventoryStacks) {							
				if(stack.hasDisplayName()) {
					String name = stack.getDisplayName();
				
					if(name.contains(this.searchQuery)) {
						this.fakeInventoryStacks.add(stack);
						this.slotOffsets.put(currentIndex, realIndex);
						currentIndex++;
					}
				}
				
				realIndex++;
			}
		}
	}

	public void onContentsChanged() {		
		this.inventoryStacks.removeIf(stack -> stack.isEmpty());	
		this.updateFakeInventory();
		this.listeners.forEach(c -> c.detectAndSendChanges());
	}
	
	@Override
	public int getSlots() {
		return this.inventoryStacks.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		int realIndex = this.getOffsettedIndex(index);
		
        if(!this.slotExists(realIndex)) {
        	return ItemStack.EMPTY;
        }
        
        return this.inventoryStacks.get(realIndex);
	}
	
	public boolean slotExists(int index) {
		if(index < 0)return false;
		if(index >= this.inventoryStacks.size())return false;
		return true;
	}

	@Override
	public ItemStack insertItem(int index, ItemStack stack, boolean simulate) {
		if(!this.slotExists(this.getOffsettedIndex(index))) {
            if(!simulate) {
            	this.inventoryStacks.add(stack);
                this.onContentsChanged();
            }
            
            return ItemStack.EMPTY;
        }
		
        ItemStack existing = this.inventoryStacks.get(this.getOffsettedIndex(index));

        int limit = this.getStackLimit(this.getOffsettedIndex(index), stack);

        if(!existing.isEmpty()) {
            if(!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                return stack;
            }

            limit -= existing.getCount();
        }

        if(limit <= 0)return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if(!simulate) {
            if(existing.isEmpty()) {
                this.inventoryStacks.set(this.getOffsettedIndex(index), 
                		reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            
            this.onContentsChanged();
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}
	
    protected int getStackLimit(int index, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(this.getOffsettedIndex(index)), stack.getMaxStackSize());
    }

	@Override
	public ItemStack extractItem(int index, int amount, boolean simulate) {
        if(amount == 0 || !this.slotExists(this.getOffsettedIndex(index))) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = this.inventoryStacks.get(this.getOffsettedIndex(index));

        if(existing.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if(existing.getCount() <= toExtract) {
            if(!simulate) {
                this.inventoryStacks.set(this.getOffsettedIndex(index), ItemStack.EMPTY);
                this.onContentsChanged();
            }
            
            return existing;
        } else {
            if(!simulate) {
                this.inventoryStacks.set(this.getOffsettedIndex(index), ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                this.onContentsChanged();
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
	}

    @Override
    public NBTTagCompound serializeNBT() {        
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();
        
        for(ItemStack stack: this.inventoryStacks) {
            if(stack == null || stack.isEmpty())continue;
            NBTTagCompound stackNBT = new NBTTagCompound();
            stack.writeToNBT(stackNBT);
            itemList.appendTag(stackNBT);
        }

        nbt.setTag("Items", itemList);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.inventoryStacks.clear();
        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        
        for(int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound stackNBT = itemList.getCompoundTagAt(i);
            this.inventoryStacks.add(new ItemStack(stackNBT));
        }
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
    	return stack.getItem() == InitItem.SPAWN_EGG_FIGHTER;
    }
    
	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	public int getOffsettedIndex(int index) {
		if(index == FAKE_SLOT)return FAKE_SLOT;
		return this.slotOffsets.containsKey(index) ? this.slotOffsets.get(index) : index;
	}
	
	@Override
	public void setStackInSlot(int index, ItemStack stack) {	
		int realIndex = this.getOffsettedIndex(index);

		if(!this.slotExists(realIndex)) {
			this.inventoryStacks.add(stack);
		} else if(this.inventoryStacks.get(realIndex).isEmpty()) {
			this.inventoryStacks.set(realIndex, stack);
		}	

		this.onContentsChanged();
	}

	public ItemStack randomEgg() {
		if(this.inventoryStacks.size() == 0)return null;
		int i = new Random().nextInt(this.inventoryStacks.size());
		ItemStack stack = this.inventoryStacks.remove(i);
		this.onContentsChanged();
		return stack;
	}
    
}