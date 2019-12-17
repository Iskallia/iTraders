package iskallia.itraders.item;

import java.util.List;
import java.util.Optional;

import iskallia.itraders.Traders;
import iskallia.itraders.entity.EntityAccelerator;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemAccelerationBottle extends Item {

	public class BottleNBT {
		public static final String SELECTED_SUB_INDEX = "SelectedSubIndex";
		public static final String SUB_LIST = "SubList";
		public static final String NAME = "Name";
		public static final String USES = "Uses";
	}

	public ItemAccelerationBottle(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote)
			return EnumActionResult.SUCCESS;

		if (world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof ITickable))
			return EnumActionResult.PASS;

		ItemStack stack = player.getHeldItem(hand);

		if (isBottleEmpty(stack))
			return EnumActionResult.SUCCESS;

		NBTTagCompound nbt = stack.getTagCompound();

		int selectedSubIndex = nbt.getInteger(BottleNBT.SELECTED_SUB_INDEX);

		NBTTagList subList = nbt.getTagList(BottleNBT.SUB_LIST, Constants.NBT.TAG_COMPOUND);

		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString(BottleNBT.NAME);
		int uses = selectedSub.getInteger(BottleNBT.USES);

		if (!useSub(world, player, pos, name))
			return EnumActionResult.SUCCESS;

		if (uses > 1) {
			if (!player.capabilities.isCreativeMode)
				selectedSub.setInteger(BottleNBT.USES, uses - 1);

			world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.35F + 0.9F);
		} else {
			if (!player.capabilities.isCreativeMode)
				subList.removeTag(selectedSubIndex);

			world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.MASTER, 1.0f, 1.0f);

			if (subList.hasNoTags()) {

				setNameWithSub(stack, "Empty");

				return EnumActionResult.SUCCESS;
			}

			nbt.setInteger(BottleNBT.SELECTED_SUB_INDEX, 0);
			String nextSub = subList.getCompoundTagAt(0).getString(BottleNBT.NAME);

			setNameWithSub(stack, nextSub);

		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		if (InitConfig.CONFIG_ACCELERATION_BOTTLE == null)
			return;

		int maxSubs = InitConfig.CONFIG_ACCELERATION_BOTTLE.MAX_CONTAINED_SUBS;
		NBTTagCompound stackNBT = stack.getTagCompound();

		if (isBottleEmpty(stack)) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "There are no subs contained.");
			return;
		}

		NBTTagList subList = stackNBT.getTagList(BottleNBT.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		int selectedSubIndex = stackNBT.getInteger(BottleNBT.SELECTED_SUB_INDEX);

		int subCount = subList.tagCount();

		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString(BottleNBT.NAME);
		int uses = selectedSub.getInteger(BottleNBT.USES);

		tooltip.add(TextFormatting.DARK_AQUA + "Sub Count" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + subCount + "/" + maxSubs);
		tooltip.add(" ");
		tooltip.add(TextFormatting.DARK_AQUA + "Selected Sub" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + name);
		tooltip.add(TextFormatting.DARK_AQUA + "Uses" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + uses);

		super.addInformation(stack, world, tooltip, flagIn);
	}

	private boolean useSub(World world, EntityPlayer player, BlockPos pos, String name) {
		int duration = InitConfig.CONFIG_ACCELERATION_BOTTLE.DURATION_PER_USE;

		Optional<EntityAccelerator> o = world.getEntitiesWithinAABB(EntityAccelerator.class, new AxisAlignedBB(pos.up())).stream().findFirst();

		if (o.isPresent()) {
			EntityAccelerator e = o.get();

			if (!e.getName().equalsIgnoreCase(name))
				return false;

			e.setTimeRemaining(e.getTimeRemaining() + (duration * 20)); // magic number 20 = ticks

			return true;

		} else {
			EntityAccelerator e = new EntityAccelerator(world, name, pos);

			IBlockState state = world.getBlockState(pos);
			EnumFacing facing = player.getAdjustedHorizontalFacing().getOpposite();
			try {
				facing = state.getValue(BlockHorizontal.FACING);
			} catch (Exception ex) {
				Traders.LOG.error("Failed to retrieve facing from the Block. Using the player's.");
			}

			e.setPosition(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);

			e.setTimeRemaining(duration * 20);
			e.setTarget(pos);
			e.setFacing(facing);

			return world.spawnEntity(e);
		}
	}

	public int getSelectedSubIndex(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;

		NBTTagCompound nbt = stack.getTagCompound();
		return nbt.getInteger(BottleNBT.SELECTED_SUB_INDEX);
	}

	public int getSubCount(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;
		NBTTagCompound nbt = stack.getTagCompound();
		NBTTagList subList = nbt.getTagList(BottleNBT.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		return subList.tagCount();
	}

	public void setSelectedSubIndex(ItemStack stack, int toSet) {
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(BottleNBT.SELECTED_SUB_INDEX, toSet);
	}

	public String getSelectedSub(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return null;

		NBTTagList subList = nbt.getTagList(BottleNBT.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		if (subList == null)
			return null;

		int index = nbt.getInteger(BottleNBT.SELECTED_SUB_INDEX);

		NBTTagCompound selectedSub = subList.getCompoundTagAt(index);
		return selectedSub.getString(BottleNBT.NAME);
	}

	public boolean isBottleEmpty(ItemStack stack) {

		if (!stack.hasTagCompound())
			return true;

		NBTTagCompound nbt = stack.getTagCompound();

		NBTTagList subList = nbt.getTagList(BottleNBT.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		if (subList == null || subList.hasNoTags())
			return true;

		return false;

	}

	public void setNameWithSub(ItemStack stack, String name) {
		stack.setStackDisplayName(TextFormatting.RESET + "" + TextFormatting.DARK_AQUA + "Soul Essence " + TextFormatting.WHITE + "(" + TextFormatting.YELLOW + name + TextFormatting.WHITE + ")");
	}

}
