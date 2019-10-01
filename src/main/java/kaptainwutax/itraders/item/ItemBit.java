package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBit extends Item {

	public final int value;
	
	public ItemBit(String name, int value) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		
		this.value = value;
	}
	
}
