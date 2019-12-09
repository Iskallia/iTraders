package iskallia.itraders.block;

import javax.annotation.Nullable;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.gui.GuiHandler;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCubeChamber extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

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

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);

        if (!world.isRemote) {
            TileEntityCubeChamber cubeChamber = getTileEntity(world, pos);

            if (cubeChamber != null) {
                boolean previousState = cubeChamber.getReceivedRedstoneSignal();
                boolean redstonePowered = world.isBlockPowered(pos);

                if (redstonePowered != previousState) { // State chanced
                    if (redstonePowered && cubeChamber.canStartInfusion())
                        cubeChamber.startPressed();

                    cubeChamber.onRedstoneSignalChanged();
                }
            }
        }
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
