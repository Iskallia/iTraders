package kaptainwutax.itraders.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import kaptainwutax.itraders.item.PouchInventory;
import kaptainwutax.itraders.world.data.DataEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEggPouch extends Container {

	protected PouchInventory pouchInventory;
	protected List<SlotItemHandler> pouchSlots = new ArrayList<>();
	protected int pouchOffset = 0;
	
	public ContainerEggPouch(World world, EntityPlayer player) {
		DataEggPouch data = DataEggPouch.get(world);
		this.pouchInventory = data.getOrCreatePouch(player);
		
		for(int row = 0; row < 6; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column;	
				SlotItemHandler slot = new SlotItemHandler(this.pouchInventory, slotId, 8 + column * 18, 18 + row * 18);
				this.addSlotToContainer(slot);
				this.pouchSlots.add(slot);
			}
		}
		
		for(int row = 0; row < 3; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column + 9;				
				this.addSlotToContainer(new Slot(player.inventory, slotId, 8 + column * 18, 140 + row * 18));
			}
		}
		
		for(int hotbar = 0; hotbar < 9; hotbar++) {
			this.addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, 198));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();

            if(index < containerSlots) {
                if(!this.mergeItemStack(stackInSlot, containerSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!this.mergeItemStack(stackInSlot, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if(stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            slot.onTake(player, stackInSlot);
        }
        
		return stack;
	}
	
	public void onMove(int amount) {
		if(amount == 0)return;
		int lastPouchOffset = this.pouchOffset;
		this.pouchOffset += amount;
		int rows = MathHelper.ceil((float)this.pouchInventory.getSlots() / 9.0f);
		this.pouchOffset = MathHelper.clamp(this.pouchOffset, 0, rows - 6);
		int diff = this.pouchOffset - lastPouchOffset;
		if(diff == 0)return;
		
		//This is super hacky, gonna have to implement a custom SlotItemHandler at some point.
		this.pouchSlots.forEach(slot -> {
			Field field = SlotItemHandler.class.getDeclaredFields()[2];
			field.setAccessible(true);
			
			try {
				field.set(slot, (int)field.get(slot) + diff * 9);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		
		this.detectAndSendChanges();
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);	
		this.pouchInventory.purge();
	}

}
