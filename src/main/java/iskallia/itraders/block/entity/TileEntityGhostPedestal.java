package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockGhostPedestal;
import iskallia.itraders.entity.EntityPedestalGhost;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityGhostPedestal extends TileInventoryBase {

    @SideOnly(Side.CLIENT)
    private EntityPedestalGhost pedestalGhost;

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

    public boolean isOccupied() {
        return !getNecklace().isEmpty();
    }

    public boolean insertNecklace(ItemStack necklaceStack) {
        if (necklaceStack.getItem() != InitItem.SKULL_NECKLACE)
            return false;

        if (isOccupied())
            return false;

        getInventoryHandler().insertItem(0, necklaceStack, false);
        return true;
    }

    public ItemStack extractNecklace() {
        return getInventoryHandler().extractItem(0, 1, false);
    }

    @Override
    public void update() {
        if (world.isRemote)
            updateClient();
    }

    protected void updateClient() {
        // Pedestal ghost was not spawned and the necklace is there!
        if ((pedestalGhost == null) && isOccupied()) {
            IBlockState blockState = world.getBlockState(pos);
            EnumFacing facing = blockState.getValue(BlockGhostPedestal.FACING);
            String name = getNecklace().getTagCompound().getString("HeadOwner");

            pedestalGhost = new EntityPedestalGhost(world);
            pedestalGhost.setPosition(pos.getX(), pos.getY(), pos.getZ());
            pedestalGhost.skin.updateSkin(name);
            pedestalGhost.name = name;
            pedestalGhost.facing = facing.getOpposite();
            pedestalGhost.blockPos = pos;

            world.spawnEntity(pedestalGhost);
        }

        // Pedestal ghost was spawned, but necklace is no longer there
        if ((pedestalGhost != null) && !isOccupied()) {
            despawnGhost();
        }
    }

    public void despawnGhost() {
        world.removeEntity(pedestalGhost);
        pedestalGhost = null;
    }

    @Override
    public void onChunkUnload() {
        if (world.isRemote) {
            if (pedestalGhost != null) {
                despawnGhost();
            }
        }
        super.onChunkUnload();
    }
}
