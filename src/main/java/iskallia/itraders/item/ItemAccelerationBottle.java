package iskallia.itraders.item;

import java.util.List;

import iskallia.itraders.Traders;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return EnumActionResult.PASS;

		ItemStack stack = player.getHeldItem(hand);

		if (!stack.hasTagCompound())
			return EnumActionResult.PASS;

		NBTTagCompound nbt = stack.getTagCompound();
		int selectedSubIndex = nbt.getInteger("SelectedSub");

		NBTTagList subList = nbt.getTagList("SubList", Constants.NBT.TAG_COMPOUND);
		if(subList.hasNoTags()) return EnumActionResult.PASS;
		
		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString("Name");
		int duration = selectedSub.getInteger("Duration");

		// TODO: use the sub for stuff
		player.sendMessage(new TextComponentString(name + ": " + duration));

		subList.removeTag(selectedSubIndex);

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound stackNBT = stack.getTagCompound();

		if (stackNBT == null || !stackNBT.hasKey("SubList")) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "This item has no data");
			return;
		}

		int selectedSubIndex = stackNBT.getInteger("SelectedSub");

		NBTTagList subList = stackNBT.getTagList("SubList", Constants.NBT.TAG_COMPOUND);

		if (subList.hasNoTags()) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "There are no subs contained.");
			return;
		}
		
		int subCount = subList.tagCount();

		NBTTagCompound selectedSub = subList.getCompoundTagAt(selectedSubIndex);

		String name = selectedSub.getString("Name");
		int duration = selectedSub.getInteger("Duration");

		int storedSeconds = duration;

		int minutes = (storedSeconds % 3600) / 60;
		int seconds = storedSeconds % 60;

		tooltip.add(TextFormatting.DARK_AQUA + "Sub Count" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + subCount + "/10");
		tooltip.add(" ");
		tooltip.add(TextFormatting.DARK_AQUA + "Selected Sub" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + name);
		tooltip.add(TextFormatting.DARK_AQUA + "Duration" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + minutes + "m " + seconds + "s");

		super.addInformation(stack, world, tooltip, flagIn);
	}

	public int getSelectedSubIndex(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;

		NBTTagCompound nbt = stack.getTagCompound();
		return nbt.getInteger("SelectedSub");
	}

	public int getSubCount(ItemStack stack) {
		if (!stack.hasTagCompound())
			return 0;
		NBTTagCompound nbt = stack.getTagCompound();
		NBTTagList subList = nbt.getTagList("SubList", Constants.NBT.TAG_COMPOUND);
		return subList.tagCount();
	}

	public void setSelectedSubIndex(ItemStack stack, int toSet) {
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("SelectedSub", toSet);
	}

	public String getSelectedSub(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return null;

		NBTTagList subList = nbt.getTagList("SubList", Constants.NBT.TAG_COMPOUND);
		if (subList == null)
			return null;

		int index = nbt.getInteger("SelectedSub");

		NBTTagCompound selectedSub = subList.getCompoundTagAt(index);
		return selectedSub.getString("Name");
	}

}
