package kaptainwutax.itraders.container.search.filter;

import kaptainwutax.itraders.container.search.SearchFilter;
import net.minecraft.item.ItemStack;

public abstract class FilterNBT extends SearchFilter {

    @Override
    public boolean compare(String stackName, ItemStack stack, String searchString) {
        try {
            double numericValue = Double.parseDouble(searchString);
            return compareNBT(stack, numericValue);

        } catch(NumberFormatException e) {
            return false;
        }
    }

    protected abstract boolean compareNBT(ItemStack stack, double value);

}
