package iskallia.itraders.container.search.filter;

import iskallia.itraders.container.search.SearchFilter;
import net.minecraft.item.ItemStack;

/**
 *
 */
public class FilterLiteral extends SearchFilter {

	@Override
	public boolean compare(String stackName, ItemStack stack, String searchString) {
		return stackName.toLowerCase().contains(searchString.toLowerCase());
	}

}
