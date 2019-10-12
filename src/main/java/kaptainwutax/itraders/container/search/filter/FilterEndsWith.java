package kaptainwutax.itraders.container.search.filter;

import kaptainwutax.itraders.container.search.SearchFilter;
import net.minecraft.item.ItemStack;

public class FilterEndsWith extends SearchFilter {

    public FilterEndsWith() {
        this.name = "endswith";
    }

    @Override
    public boolean compare(String stackName, ItemStack stack, String searchString) {
        return stackName.toLowerCase().endsWith(searchString.toLowerCase());
    }

}
