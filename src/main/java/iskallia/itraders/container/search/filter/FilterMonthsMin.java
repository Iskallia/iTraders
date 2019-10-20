package iskallia.itraders.container.search.filter;

import net.minecraft.item.ItemStack;

public class FilterMonthsMin extends FilterMonths {

	public FilterMonthsMin() {
		this.name = "monthsmin";
	}

	@Override
	protected boolean compareNBT(ItemStack stack, double value) {
		double stackMonths = extractMonths(stack);
		return stackMonths != -1 && stackMonths >= value;
	}

}
