package iskallia.itraders.block;

import iskallia.itraders.Traders;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePartDirectional extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<BlockDoublePartDirectional.EnumPartType> PART = PropertyEnum.create("part", BlockDoublePartDirectional.EnumPartType.class);
	
	public static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0, -1, 0, 1, 1, 1);
	public static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0, 0, 0, 1, 2, 1);
	
	public BlockDoublePartDirectional(String name, Material material) {
		super(material);
		this.setRegistryName(Traders.getResource(name));
		this.setUnlocalizedName(name);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(PART, BlockDoublePartDirectional.EnumPartType.BOTTOM)
				.withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(PART) == EnumPartType.BOTTOM ? BOTTOM_AABB : TOP_AABB;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			IBlockState north = world.getBlockState(pos.north());
			IBlockState south = world.getBlockState(pos.south());
			IBlockState west = world.getBlockState(pos.west());
			IBlockState east = world.getBlockState(pos.east());
			EnumFacing facing = state.getValue(FACING);
			
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
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(PART).ordinal() << 2) | state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(PART, BlockCryoChamber.EnumPartType.values()[meta >> 2])
				.withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PART, FACING);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntity(World world, BlockPos pos, IBlockState state) {
		BlockPos tePos = state.getValue(PART) == EnumPartType.TOP ? pos.down() : pos;
		
		TileEntity tileEntity = world.getTileEntity(tePos);
		
		if (tileEntity == null)
			return null;
		
		return (T)tileEntity;
	}
	
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
