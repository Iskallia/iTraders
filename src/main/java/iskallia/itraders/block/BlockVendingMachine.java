package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityVendingMachine;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVendingMachine extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);

    public static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
    public static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0, 0, 0, 1, 2, 1);

    public BlockVendingMachine(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setHardness(2f);

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(PART, EnumPartType.BOTTOM)
                .withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(PART) == EnumPartType.BOTTOM
                ? BOTTOM_AABB : TOP_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(PART) == EnumPartType.BOTTOM
                ? BOTTOM_AABB : TOP_AABB;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(PART).ordinal() << 2)
                | state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(PART, EnumPartType.values()[meta >> 2])
                .withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART, FACING);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            IBlockState north = world.getBlockState(pos.north());
            IBlockState south = world.getBlockState(pos.south());
            IBlockState west = world.getBlockState(pos.west());
            IBlockState east = world.getBlockState(pos.east());
            EnumFacing facing = state.getValue(FACING);
            System.out.println(facing);

            if (facing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
                facing = EnumFacing.SOUTH;
            } else if (facing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
                facing = EnumFacing.NORTH;
            } else if (facing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
                facing = EnumFacing.EAST;
            } else if (facing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
                facing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, facing), 2);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TileEntityVendingMachine tileEntity = getTileEntity(world, pos, state);
            System.out.println(tileEntity.getNickname());
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (state.getValue(PART) == EnumPartType.BOTTOM) {
            TileEntityVendingMachine tileEntity = getTileEntity(world, pos, state);

            if(tileEntity != null) {
                // TODO: drop item
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState stateObserved, World world, BlockPos posObserved, Block blockChanged, BlockPos fromPos) {
        if (blockChanged != InitBlock.VENDING_MACHINE)
            return; // No need to handle, anything other than Vending Machine was changed

        EnumPartType observedPart = stateObserved.getValue(PART);
        BlockPos posOtherPart = observedPart == EnumPartType.BOTTOM ? posObserved.up() : posObserved.down();
        IBlockState stateOtherPart = world.getBlockState(posOtherPart);

        // Block was changed from Vending Machine to something else
        if (stateOtherPart.getBlock() != InitBlock.VENDING_MACHINE) {
            world.setBlockToAir(posObserved);

            if (!world.isRemote)
                dropBlockAsItem(world, posObserved, stateObserved, 0);
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityVendingMachine();
    }

    public static TileEntityVendingMachine getTileEntity(World world, BlockPos pos, IBlockState state) {
        BlockPos tePos = state.getValue(PART) == EnumPartType.TOP ? pos.down() : pos;

        TileEntity tileEntity = world.getTileEntity(tePos);

        if (!(tileEntity instanceof TileEntityVendingMachine))
            return null;

        return (TileEntityVendingMachine) tileEntity;
    }

    public static void placeVendingMachine(World world, BlockPos pos, EnumFacing facing, Block block) {
        BlockPos posTopPart = pos.up();
        IBlockState blockstate = block.getDefaultState().withProperty(FACING, facing);

        world.setBlockState(pos, blockstate.withProperty(PART, EnumPartType.BOTTOM), 2);
        world.setBlockState(posTopPart, blockstate.withProperty(PART, EnumPartType.TOP), 2);

        world.notifyNeighborsOfStateChange(pos, block, false);
        world.notifyNeighborsOfStateChange(posTopPart, block, false);
    }

    // Proudly copied from Jmil's snippet :p -Goodie
    public enum EnumPartType implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String name;

        EnumPartType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

    }

}
