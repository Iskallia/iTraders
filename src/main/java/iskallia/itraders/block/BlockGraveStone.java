package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityGraveStone;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityGraveStone();
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
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	drops.clear();
    }
    
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

    	TileEntity tileEntity = world.getTileEntity(pos);
    	
    	if(tileEntity != null && tileEntity instanceof TileEntityGraveStone) {
    		TileEntityGraveStone graveStoneTE = (TileEntityGraveStone)tileEntity;
    		NBTTagCompound nbt = stack.getTagCompound();
    		
            if(stack.hasDisplayName()) {
            	graveStoneTE.setName(stack.getDisplayName());
            }
            
            if(nbt != null && nbt.hasKey("Months", Constants.NBT.TAG_INT)) {
            	graveStoneTE.setMonths(nbt.getInteger("Months"));
            } 
            
            if(nbt != null && nbt.hasKey("BlocksMined", Constants.NBT.TAG_INT)) {
            	graveStoneTE.setBlocksMined(nbt.getInteger("BlocksMined"));
            } 
    	}
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {    	
    	TileEntity tileEntity = world.getTileEntity(pos);
    	
    	if(tileEntity != null && tileEntity instanceof TileEntityGraveStone) {
    		TileEntityGraveStone graveStoneTE = (TileEntityGraveStone)tileEntity;
        	
        	ItemStack dropStack = new ItemStack(InitBlock.ITEM_GRAVE_STONE, 1);
        	NBTTagCompound nbt = new NBTTagCompound();
        	
        	if(graveStoneTE.getMonths() != -1) {
        		nbt.setInteger("Months", graveStoneTE.getMonths());
        	}
        	
        	if(graveStoneTE.getBlocksMined() != -1) {
        		nbt.setInteger("BlocksMined", graveStoneTE.getBlocksMined());
        	}
        	
        	dropStack.setTagCompound(nbt);     	
        	
        	if(graveStoneTE.getName() != null) {
            	dropStack.setStackDisplayName(graveStoneTE.getName());
        	}
        	
    		Block.spawnAsEntity(world, pos, dropStack);
    	} 	

    	super.breakBlock(world, pos, state);
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
