package iskallia.itraders.container;

import iskallia.itraders.container.slot.FakeSlot;
import iskallia.itraders.container.slot.PouchSlot;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.net.packet.S2CPouchScroll;
import iskallia.itraders.world.data.DataEggPouch;
import iskallia.itraders.world.storage.PouchInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerEggPouch extends Container {

	public EntityPlayer player;
	private World world;

	public PouchInventory pouchInventory;

	public ContainerEggPouch(World world, EntityPlayer player) {
		this.world = world;
		this.player = player;

		this.pouchInventory = world.isRemote ? new PouchInventory(true)
				: DataEggPouch.get(world).getOrCreatePouch(player);

		for (int row = 0; row < 6; row++) {
			for (int column = 0; column < 9; column++) {
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

		if (!this.world.isRemote)
			this.pouchInventory.addListener(this);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(stackInSlot, containerSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stackInSlot, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (stackInSlot.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			slot.onTake(player, stackInSlot);

		}

		return stack;
	}

	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (!stack.isEmpty()) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot = this.inventorySlots.get(i);
				ItemStack stackInSlot = slot.getStack();

				if (!stackInSlot.isEmpty() && stackInSlot.getItem() == stack.getItem()
						&& (!stack.getHasSubtypes() || stack.getMetadata() == stackInSlot.getMetadata())
						&& ItemStack.areItemStackTagsEqual(stack, stackInSlot)) {

					int j = stackInSlot.getCount() + stack.getCount();
					int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

					if (j <= maxSize) {
						stack.setCount(0);
						stackInSlot.setCount(j);
						slot.onSlotChanged();
						flag = true;
					} else if (stackInSlot.getCount() < maxSize) {
						stack.shrink(maxSize - stackInSlot.getCount());
						stackInSlot.setCount(maxSize);
						slot.onSlotChanged();
						flag = true;
					}
				}

				i += reverseDirection ? -1 : 1;
			}
		}

		if (!stack.isEmpty()) {
			i = reverseDirection ? endIndex - 1 : startIndex;

			while (true) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}

				Slot slot = this.inventorySlots.get(i);
				ItemStack stackInSlot = slot.getStack();

				if (stackInSlot.isEmpty() && slot.isItemValid(stack)) {
					if (stack.getCount() > slot.getSlotStackLimit()) {
						slot.putStack(stack.splitStack(slot.getSlotStackLimit()));
					} else {
						slot.putStack(stack.splitStack(stack.getCount()));
					}

					slot.onSlotChanged();
					flag = true;
					break;
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}

	@Override
	public void detectAndSendChanges() {
		this.pouchInventory.onContentsChanged();
		super.detectAndSendChanges();

		for (int i = 0; i < this.inventorySlots.size(); ++i) {
			for (int j = 0; j < this.listeners.size(); ++j) {
				if (this.inventorySlots.get(i) instanceof PouchSlot) {
					((IContainerListener) this.listeners.get(j)).sendSlotContents(this, i,
							this.inventorySlots.get(i).getStack());
				}
			}
		}
	}

	public void onMove(int amount) {
		this.pouchInventory.move(amount);

		if (!this.world.isRemote) {
			InitPacket.PIPELINE.sendTo(
					new S2CPouchScroll(this.pouchInventory.currentScroll, this.pouchInventory.totalScroll),
					(EntityPlayerMP) this.player);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!this.world.isRemote)
			this.pouchInventory.removeListener(this);
	}

}
