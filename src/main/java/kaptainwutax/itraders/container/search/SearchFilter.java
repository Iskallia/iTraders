package kaptainwutax.itraders.container.search;

import net.minecraft.item.ItemStack;

public abstract class SearchFilter {

    public String name;

    public boolean test(String stackName, ItemStack stack, String searchQuery) {
        return this.compare(stackName, stack, getSearchString(searchQuery));
    }

    private String getSearchString(String searchQuery) {
        if (!searchQuery.startsWith("@")) {
            return searchQuery;
        }

        if(!searchQuery.contains(" ")) {
            return searchQuery;
        }

        return searchQuery.substring(searchQuery.indexOf(" ")).trim();
    }

    public abstract boolean compare(String stackName, ItemStack stack, String searchString);

}
