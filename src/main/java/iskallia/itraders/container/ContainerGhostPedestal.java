package iskallia.itraders.container;

import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGhostPedestal extends Container {

    private World world;
	private EntityPlayer player;
	private TileEntityGhostPedestal pedestal;
	
	private InventoryPlayer playerInventory;
	private IItemHandler pedestalInventory;

	public ContainerGhostPedestal(World world, EntityPlayer player, TileEntityGhostPedestal pedestal) {
    	this.world = world;
    	this.player = player;
    	this.pedestal = pedestal;
    	
    	this.playerInventory = player.inventory;
    	this.pedestalInventory = pedestal.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, player.getHorizontalFacing());
        
    	int i = 51;

    	this.addSlotToContainer(new SlotItemHandler(this.pedestalInventory, 0, 44 + 2 * 18, 20));

        for(int l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(this.playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(this.playerInventory, i1, 8 + i1 * 18, 109));
        }
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
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
