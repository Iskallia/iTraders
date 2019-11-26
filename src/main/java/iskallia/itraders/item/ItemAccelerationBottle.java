package iskallia.itraders.item;

import java.util.List;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemAccelerationBottle extends Item {

	public ItemAccelerationBottle(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound stackNBT = stack.getTagCompound();

		if (stackNBT == null || !stackNBT.hasKey("duration") || !stackNBT.hasKey("speed")) {
			tooltip.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "This item has no data");
			return;
		}

		int duration = stackNBT.getInteger("duration");
		int speed = stackNBT.getInteger("speed");
		
		int storedSeconds = duration / 20;

		int hours = storedSeconds / 3600;
		int minutes = (storedSeconds % 3600) / 60;
		int seconds = storedSeconds % 60;

		tooltip.add(TextFormatting.DARK_AQUA + "Duration" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + minutes + "m " + seconds + "s");
		tooltip.add(TextFormatting.DARK_AQUA + "Speed" + TextFormatting.GRAY + ": " + TextFormatting.YELLOW + "+" + speed + "%");

		super.addInformation(stack, world, tooltip, flagIn);
	}

}
