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

	protected PouchInventory pouchInventory;
	protected List<SlotItemHandler> pouchSlots = new ArrayList<>();

	public int currentScroll = 1;
	public int totalScroll = 1;

	public @Nonnull	String searchQuery = "";

	public ContainerEggPouch(World world, EntityPlayer player) {
		this.world = world;
		this.player = player;

		DataEggPouch data = DataEggPouch.get(world);
		this.pouchInventory = data.getOrCreatePouch(player);

		updateSlots();
	}

	public boolean inSearchMode() {
		return !searchQuery.isEmpty();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        boolean fromPlayerInventory = 54 <= index && index <= 89;
        boolean fromEggPouch = 0 <= index && index <= 53;
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

			int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();

			if (fromEggPouch) {
				if (!this.mergeItemStack(stackInSlot, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}

			} else if (fromPlayerInventory) {
				if(inSearchMode())
					return  ItemStack.EMPTY;

				if(stack.getItem() instanceof ItemSpawnEggFighter) {
					this.pouchInventory.putStackOnFirstEmpty(stack);
					slot.putStack(ItemStack.EMPTY);
					this.detectAndSendChanges();
				}
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

	public void updateSlots() {
		this.pouchSlots.clear();
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();

		if(inSearchMode()) {
			Iterator<Integer> filteredIndices = this.pouchInventory.filterIndices(searchQuery).iterator();
			for (int i = 0; i < 54; i++) {
				int row = i / 9;
				int col = i % 9;
				if(filteredIndices.hasNext()) {
					int index = filteredIndices.next();
					SlotItemHandler slot = new SlotItemHandler(this.pouchInventory, index, 8 + col * 18, 28 + row * 18);
					this.addSlotToContainer(slot);
					this.pouchSlots.add(slot);
				} else {
					// TODO: put empty slots instead of index#0 (?)
					int index = 0;
					SlotItemHandler slot = new SlotItemHandler(this.pouchInventory, index, 8 + col * 18, 28 + row * 18);
					this.addSlotToContainer(slot);
					this.pouchSlots.add(slot);
				}
			}

		} else {
			for(int row = 0; row < 6; row++) {
				for(int column = 0; column < 9; column++) {
					int slotId = row * 9 + column;
					SlotItemHandler slot = new SlotItemHandler(this.pouchInventory, slotId, 8 + column * 18, 28 + row * 18);
					this.addSlotToContainer(slot);
					this.pouchSlots.add(slot);
				}
			}
		}

		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				int slotId = row * 9 + column + 9;
				this.addSlotToContainer(new Slot(player.inventory, slotId, 8 + column * 18, 140 + row * 18));
			}
		}

		for (int hotbar = 0; hotbar < 9; hotbar++) {
			this.addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, 198));
		}

		detectAndSendChanges();
	}

	public void onMove(int amount) {
		this.pouchInventory.rescaleToFit();
		int lastScroll = this.currentScroll;
		this.currentScroll += amount;
		this.totalScroll = MathHelper.ceil((float)this.pouchInventory.getSlots() / 9.0f) - 5;
		this.currentScroll = MathHelper.clamp(this.currentScroll, 1, this.totalScroll);
		int diff = this.currentScroll - lastScroll;
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

		if(!this.world.isRemote) {
			InitPacket.PIPELINE.sendTo(new S2CPouchScroll(this.currentScroll, this.totalScroll), (EntityPlayerMP)this.player);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.pouchInventory.purge();
	}

}
