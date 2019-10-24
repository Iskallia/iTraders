package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityGhostPedestal extends TileInventoryBase {

    public TileEntityGhostPedestal() {
        super(1);
    }

    @Override
    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTileFiltered(this) {
            @Override
            public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
                return existing.isEmpty() && (toAdd.getItem() == InitItem.SKULL_NECKLACE);
            }
        };
    }

    @Nonnull
    public ItemStack getNecklace() {
        return getInventoryHandler().getStackInSlot(0);
    }

}
