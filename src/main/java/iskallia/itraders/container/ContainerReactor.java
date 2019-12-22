package iskallia.itraders.container;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.entity.TileEntityPowerCube;
import iskallia.itraders.container.slot.ReactorSlot;
import iskallia.itraders.multiblock.reactor.MultiblockReactor;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ContainerReactor extends Container {

    //    public static final Map<BlockPos, Integer> OFFSET_TO_SLOT_INDEX_MAP = new HashMap<>();
    public static final BiMap<BlockPos, Integer> OFFSET_TO_SLOT_INDEX_MAP = HashBiMap.create(24);

    static { // Laziness strikes again! xd
        int index = 0;
        for (int y = 1; y >= -1; y--) {
            for (int z = 1; z >= -1; z--) {
                for (int x = 1; x >= -1; x--) {
                    if (x == 0 && z == 0) continue;
                    OFFSET_TO_SLOT_INDEX_MAP.put(
                            new BlockPos(x, y, z),
                            index++
                    );
                }
            }
        }
    }

    public ContainerReactor(World world, EntityPlayer player, TileEntityReactorCore reactorCore) {
        InventoryPlayer playerInventory = player.inventory;

        int xOffset = 0;

        // Top Power Cubes
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 0, xOffset + 8, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 1, xOffset + 26, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 2, xOffset + 44, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 3, xOffset + 8, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 4, xOffset + 44, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 5, xOffset + 8, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 6, xOffset + 26, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 7, xOffset + 44, 55));

        xOffset += 62;

        // Middle Power Cubes
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 8, xOffset + 8, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 9, xOffset + 26, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 10, xOffset + 44, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 11, xOffset + 8, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 12, xOffset + 44, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 13, xOffset + 8, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 14, xOffset + 26, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 15, xOffset + 44, 55));

        xOffset += 62;

        // Bottom Power Cubes
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 16, xOffset + 8, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 17, xOffset + 26, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 18, xOffset + 44, 19));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 19, xOffset + 8, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 20, xOffset + 44, 37));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 21, xOffset + 8, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 22, xOffset + 26, 55));
        this.addSlotToContainer(new ReactorSlot(world, reactorCore, 23, xOffset + 44, 55));

        // Player inventory upper
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int index = col + row * 9 + 9;
                int viewportX = col * 18 + 16;
                int viewportY = row * 18 + 105;
                this.addSlotToContainer(new Slot(playerInventory, index, viewportX, viewportY));
            }
        }

        // Player inventory hotbar
        for (int i = 0; i < 9; ++i) {
            int viewportX = i * 18 + 16;
            this.addSlotToContainer(new Slot(playerInventory, i, viewportX, 163));
        }
    }

    @Nonnull
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
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

}
