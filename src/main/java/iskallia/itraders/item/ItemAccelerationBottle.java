package iskallia.itraders.item;

import java.util.List;
import java.util.Optional;

import iskallia.itraders.Traders;
import iskallia.itraders.entity.EntityAccelerator;
import iskallia.itraders.init.InitItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemAccelerationBottle extends Item {

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

		ItemStack stack = player.getHeldItem(hand);

		if (!stack.hasTagCompound())
			return EnumActionResult.PASS;

		NBTTagCompound nbt = stack.getTagCompound();
		int selectedSubIndex = nbt.getInteger(NBTConstants.SELECTED_SUB_INDEX);

		NBTTagList subList = nbt.getTagList(NBTConstants.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		if (subList.hasNoTags())
			return EnumActionResult.PASS;

		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString(NBTConstants.NAME);
		int uses = selectedSub.getInteger(NBTConstants.USES);

		// TODO: use the sub for stuff
		Optional<EntityAccelerator> o = world.getEntitiesWithinAABB(EntityAccelerator.class, new AxisAlignedBB(pos.up())).stream().findFirst();

		if (o.isPresent()) {
			EntityAccelerator e = o.get();
			if (!e.getName().equalsIgnoreCase(name))
				return EnumActionResult.PASS;

			e.setTimeRemaining(e.getTimeRemaining() + 10 * 20);

		} else {
			EntityAccelerator e = new EntityAccelerator(world, name, pos);
			e.setTimeRemaining(10 * 20);

			world.spawnEntity(e);
		}

		if (uses > 1)
			selectedSub.setInteger(NBTConstants.USES, uses - 1);
		else
			subList.removeTag(selectedSubIndex);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound stackNBT = stack.getTagCompound();

		if (stackNBT == null || !stackNBT.hasKey(NBTConstants.SUB_LIST)) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "This item has no data");
			return;
		}

		int selectedSubIndex = stackNBT.getInteger(NBTConstants.SELECTED_SUB_INDEX);

		NBTTagList subList = stackNBT.getTagList(NBTConstants.SUB_LIST, Constants.NBT.TAG_COMPOUND);

		if (subList.hasNoTags()) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "There are no subs contained.");
			return;
		}

		int subCount = subList.tagCount();

		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString(NBTConstants.NAME);
		int uses = selectedSub.getInteger(NBTConstants.USES);

		tooltip.add(TextFormatting.DARK_AQUA + "Sub Count" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + subCount + "/10");
		tooltip.add(" ");
		tooltip.add(TextFormatting.DARK_AQUA + "Selected Sub" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + name);
		tooltip.add(TextFormatting.DARK_AQUA + "Uses" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + uses);

		super.addInformation(stack, world, tooltip, flagIn);
	}

	public int getSelectedSubIndex(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;

		NBTTagCompound nbt = stack.getTagCompound();
		return nbt.getInteger(NBTConstants.SELECTED_SUB_INDEX);
	}

	public int getSubCount(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;
		NBTTagCompound nbt = stack.getTagCompound();
		NBTTagList subList = nbt.getTagList(NBTConstants.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		return subList.tagCount();
	}

	public void setSelectedSubIndex(ItemStack stack, int toSet) {
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(NBTConstants.SELECTED_SUB_INDEX, toSet);
	}

	public String getSelectedSub(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return null;

		NBTTagList subList = nbt.getTagList(NBTConstants.SUB_LIST, Constants.NBT.TAG_COMPOUND);
		if (subList == null)
			return null;

		int index = nbt.getInteger(NBTConstants.SELECTED_SUB_INDEX);

		NBTTagCompound selectedSub = subList.getCompoundTagAt(index);
		return selectedSub.getString(NBTConstants.NAME);
	}

	public class NBTConstants {

		public static final String SELECTED_SUB_INDEX = "SelectedSubIndex";
		public static final String SUB_LIST = "SubList";
		public static final String NAME = "Name";
		public static final String USES = "Uses";

	}

}
