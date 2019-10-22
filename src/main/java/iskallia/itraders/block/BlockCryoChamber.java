package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.block.entity.TileEntityCryoChamber;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockCryoChamber extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<BlockCryoChamber.EnumPartType> PART = PropertyEnum.<BlockCryoChamber.EnumPartType>create("part", BlockCryoChamber.EnumPartType.class);

	public BlockCryoChamber(String name, Material materialIn) {
		super(materialIn);
		this.setRegistryName(Traders.getResource(name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockCryoChamber.EnumPartType.BOTTOM));
	}

	public int getMetaFromState(IBlockState state) {
		return (state.getValue(PART).ordinal() << 2) | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PART, BlockCryoChamber.EnumPartType.values()[meta >> 2]).withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PART, FACING });
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

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityCryoChamber();
	}

	public enum EnumPartType implements IStringSerializable {

		TOP("top"), BOTTOM("bottom");

		private final String name;

		private EnumPartType(String name) {
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
