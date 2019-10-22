package iskallia.itraders.container.search.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FilterMonths extends FilterNBT {

	public FilterMonths() {
		this.name = "months";
	}

	@Override
	protected boolean compareNBT(ItemStack stack, double value) {
		double stackMonths = extractMonths(stack);
		return stackMonths != -1 && stackMonths == value;
	}

	protected double extractMonths(ItemStack stack) {
		NBTTagCompound stackNBT = stack.getTagCompound();

		if (!stackNBT.hasKey("EntityTag", 10)) {
			return -1;
		}

		NBTTagCompound entityTagNBT = stackNBT.getCompoundTag("EntityTag");

		if (!entityTagNBT.hasKey("SubData", 10)) {
			return -1;
		}

		NBTTagCompound subDataNBT = entityTagNBT.getCompoundTag("SubData");

		if (!subDataNBT.hasKey("Months")) {
			return -1;
		}

		return subDataNBT.getDouble("Months");
	}

}
