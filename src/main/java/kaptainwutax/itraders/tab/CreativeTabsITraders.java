package kaptainwutax.itraders.tab;

import kaptainwutax.itraders.init.InitItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabsITraders extends CreativeTabs {

	public CreativeTabsITraders(String name) {
		super(name);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(InitItem.SPAWN_EGG_FIGHTER, 1);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

}
