package kaptainwutax.itraders.container.search.filter;

import kaptainwutax.itraders.container.search.SearchFilter;
import net.minecraft.item.ItemStack;

public class FilterStartsWith extends SearchFilter {

    public FilterStartsWith() {
        this.name = "startswith";
    }

    @Override
    public boolean compare(String stackName, ItemStack stack, String searchString) {
        return stackName.toLowerCase().startsWith(searchString.toLowerCase());
    }
}
