package iskallia.itraders.container;

import iskallia.itraders.block.entity.TileEntityCubeChamber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCubeChamber extends Container {

    private World world;
    private EntityPlayer player;
    private TileEntityCubeChamber cubeChamber;

    private InventoryPlayer inventoryPlayer;

    public ContainerCubeChamber(World world, EntityPlayer player, TileEntityCubeChamber cubeChamber) {
        this.world = world;
        this.player = player;
        this.cubeChamber = cubeChamber;

        this.inventoryPlayer = player.inventory;

        IItemHandler inputCubeChamber = cubeChamber.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        IItemHandler outputCubeChamber = cubeChamber.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

        this.addSlotToContainer(new SlotItemHandler(inputCubeChamber, 0, 65, 21)); // 0 = Input
        this.addSlotToContainer(new SlotItemHandler(inputCubeChamber, 1, 43, 21)); // 1 = Booster
        this.addSlotToContainer(new SlotItemHandler(outputCubeChamber, 2, 130, 21)); // 2 = Output

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(inventoryPlayer, k, 8 + k * 18, 142));
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

            if (stackInSlot.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();


            slot.onTake(player, stackInSlot);
        }

        return stack;
    }


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
