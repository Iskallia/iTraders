package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockDoublePartDirectional;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChamber extends Item {
	public Block chamber_block;
	
	public ItemChamber(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		}
		
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (!block.isReplaceable(world, pos)) {
			pos = pos.offset(facing);
		}
		
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (chamber_block == null)
			return EnumActionResult.FAIL;
		
		if (
				player.canPlayerEdit(pos, facing, itemstack) 
				&& chamber_block.canPlaceBlockAt(world, pos)) {
			EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
			
			placeChamber(world, pos, enumfacing, chamber_block);
			
			SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			
			itemstack.shrink(1);
			
			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.FAIL;
		}
	}
	
	public static void placeChamber(World world, BlockPos pos, EnumFacing facing, Block block) {
		BlockPos blockpos2 = pos.up();
		IBlockState iblockstate = block.getDefaultState().withProperty(BlockDoublePartDirectional.FACING, facing);
		
		world.setBlockState(pos, iblockstate.withProperty(BlockDoublePartDirectional.PART, BlockDoublePartDirectional.EnumPartType.BOTTOM), 2);
		world.setBlockState(blockpos2, iblockstate.withProperty(BlockDoublePartDirectional.PART, BlockDoublePartDirectional.EnumPartType.TOP), 2);
		
		world.notifyNeighborsOfStateChange(pos, block, false);
		world.notifyNeighborsOfStateChange(blockpos2, block, false);
	}
}
