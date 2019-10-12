package kaptainwutax.itraders.container.search.filter;

import net.minecraft.item.ItemStack;

public class FilterMonthsMax extends FilterMonths {

    public FilterMonthsMax() {
        this.name = "monthsmax";
    }

    @Override
    protected boolean compareNBT(ItemStack stack, double value) {
        double stackMonths = extractMonths(stack);
        return stackMonths != -1 && stackMonths <= value;
    }

}
