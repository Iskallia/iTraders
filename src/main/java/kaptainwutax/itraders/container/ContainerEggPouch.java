package kaptainwutax.itraders.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import kaptainwutax.itraders.item.PouchInventory;
import kaptainwutax.itraders.net.packet.S2CPouchScroll;
import kaptainwutax.itraders.world.data.DataEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerEggPouch extends Container {

	private EntityPlayer player;
	private World world;

	public PouchInventory pouchInventory;

	public ContainerEggPouch(World world, EntityPlayer player) {
		this.world = world;
		this.player = player;

		this.pouchInventory = world.isRemote ? new PouchInventory() : DataEggPouch.get(world).getOrCreatePouch(player);
		
		for(int row = 0; row < 6; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column;
				PouchSlot slot = new PouchSlot(this.pouchInventory, slotId, 8 + column * 18, 28 + row * 18);
				this.addSlotToContainer(slot);
			}
		}

		FakeSlot fakeSlot = new FakeSlot(this.pouchInventory, 0, 0);
		this.addSlotToContainer(fakeSlot);
		
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				int slotId = row * 9 + column + 9;
				this.addSlotToContainer(new Slot(player.inventory, slotId, 8 + column * 18, 140 + row * 18));
			}
		}
		
		for (int hotbar = 0; hotbar < 9; hotbar++) {
			this.addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, 198));
		}		

		this.pouchInventory.addListener(this);
		this.detectAndSendChanges();
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
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	public void onMove(int amount) {
		this.pouchInventory.move(amount);
		this.pouchInventory.onContentsChanged();
		if(!this.world.isRemote) {
			InitPacket.PIPELINE.sendTo(new S2CPouchScroll(this.pouchInventory.currentScroll, this.pouchInventory.totalScroll), (EntityPlayerMP)this.player);
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.pouchInventory.removeListener(this);
	}

}
