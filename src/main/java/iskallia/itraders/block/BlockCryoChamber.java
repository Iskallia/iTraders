package iskallia.itraders.block;

import java.util.Random;

import iskallia.itraders.block.entity.TileEntityCryoChamber;
import iskallia.itraders.block.entity.TileEntityVendingMachine;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockCryoChamber extends BlockDoublePartDirectional {
	
	public BlockCryoChamber(String name, Material material) {
		super(name, material);
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setHardness(2f);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityCryoChamber teCryoChamber = getTileEntity(world, pos, state);
			
			if (teCryoChamber == null)
				return true;
			
			if (teCryoChamber.state == TileEntityCryoChamber.CryoState.EMPTY) {
				ItemStack heldStack = player.getHeldItem(hand);
				if (teCryoChamber.insertEgg(heldStack) && !player.isCreative()) {
					player.setHeldItem(hand, ItemStack.EMPTY);
				}
			} else if (teCryoChamber.state == TileEntityCryoChamber.CryoState.DONE) {
				ItemStack cardStack = teCryoChamber.extractContent();
				if (cardStack != ItemStack.EMPTY) {
					player.addItemStackToInventory(cardStack);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (state.getValue(PART) == EnumPartType.BOTTOM) {
			TileEntityCryoChamber tileEntity = getTileEntity(world, pos, state);
			
			if (tileEntity != null) {
				ItemStack contentStack = tileEntity.getContent();
				if (!contentStack.isEmpty()) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), contentStack);
				}
			}
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState stateObserved, World world, BlockPos posObserved, Block blockChanged, BlockPos fromPos) {
		if (blockChanged != InitBlock.CRYO_CHAMBER)
			return; // No need to handle, anything other than Cryo-chamber was changed
		
		EnumPartType observedPart = stateObserved.getValue(PART);
		BlockPos posOtherPart = observedPart == EnumPartType.BOTTOM ? posObserved.up() : posObserved.down();
		IBlockState stateOtherPart = world.getBlockState(posOtherPart);
		
		// Block was changed from Cryo-chamber to something else
		if (stateOtherPart.getBlock() != InitBlock.CRYO_CHAMBER) {
			world.setBlockToAir(posObserved);
			
			if (!world.isRemote)
				dropBlockAsItem(world, posObserved, stateObserved, 0);
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(PART) == EnumPartType.BOTTOM ? InitItem.CRYO_CHAMBER : Items.AIR;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(InitItem.CRYO_CHAMBER);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityCryoChamber();
	}
}
