package iskallia.itraders.block;

import javax.annotation.Nullable;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.gui.GuiHandler;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockCubeChamber extends Block {

    public static TileEntityCubeChamber getTileEntity(World world, BlockPos pos) {
        if (world == null)
            return null;

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null)
            return null;

        if (!(tileEntity instanceof TileEntityCubeChamber))
            return null;

        return (TileEntityCubeChamber) tileEntity;
    }

    public BlockCubeChamber(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setHardness(2f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCubeChamber) {
            playerIn.openGui(Traders.getInstance(), GuiHandler.POWER_CHAMBER, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }

        return super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityCubeChamber cubeChamber = getTileEntity(world, pos);

        if (cubeChamber != null) {
            for (int i = 0; i < cubeChamber.getInventoryHandler().getSlots(); i++) {
                ItemStack stackInSlot = cubeChamber.getInventoryHandler().getStackInSlot(i);
                if (!stackInSlot.isEmpty())
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stackInSlot);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(InitBlock.ITEM_CUBE_CHAMBER);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCubeChamber();
    }
}
