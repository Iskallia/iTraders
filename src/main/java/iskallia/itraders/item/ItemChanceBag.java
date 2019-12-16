package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

public class ItemChanceBag extends Item {
	
	private EnumRarity rarity;

	public ItemChanceBag(String name, EnumRarity rarity) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
		
		this.rarity = rarity;
	}

}
