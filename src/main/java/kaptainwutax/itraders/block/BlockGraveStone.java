package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGraveStone extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public BlockGraveStone(String name, Material material) {
		super(material);
		this.setRegistryName(Traders.getResource(name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing facing = (EnumFacing)state.getValue(FACING);

            if(facing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
                facing = EnumFacing.SOUTH;
            } else if(facing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
                facing = EnumFacing.NORTH;
            } else if(facing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
                facing = EnumFacing.EAST;
            } else if(facing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
                facing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, facing), 2);
        }
    }
    
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if(stack.hasDisplayName()) {
        	//TODO: Pass on data to the tile entity.
        }
    }
    
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
    
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);

        if(facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

}
