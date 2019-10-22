package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import iskallia.itraders.block.BlockCryoChamber;
import kaptainwutax.itraders.init.InitBlock;
import kaptainwutax.itraders.init.InitItem;
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

public class ItemCryoChamber extends Item {

	public ItemCryoChamber(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}

			ItemStack itemstack = player.getHeldItem(hand);

			if (player.canPlayerEdit(pos, facing, itemstack) && InitBlock.CRYO_CHAMBER.canPlaceBlockAt(worldIn, pos)) {
				EnumFacing enumfacing = EnumFacing.fromAngle((double) player.rotationYaw);
				placeCryoChamber(worldIn, pos, enumfacing, InitBlock.CRYO_CHAMBER);
				SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
				return EnumActionResult.SUCCESS;
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	public static void placeCryoChamber(World worldIn, BlockPos pos, EnumFacing facing, Block block) {
		BlockPos blockpos2 = pos.up();
		IBlockState iblockstate = block.getDefaultState().withProperty(BlockCryoChamber.FACING, facing);
		worldIn.setBlockState(pos, iblockstate.withProperty(BlockCryoChamber.PART, BlockCryoChamber.EnumPartType.BOTTOM), 2);
		worldIn.setBlockState(blockpos2, iblockstate.withProperty(BlockCryoChamber.PART, BlockCryoChamber.EnumPartType.TOP), 2);
		worldIn.notifyNeighborsOfStateChange(pos, block, false);
		worldIn.notifyNeighborsOfStateChange(blockpos2, block, false);
	}

}
