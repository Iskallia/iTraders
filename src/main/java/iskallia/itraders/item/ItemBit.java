package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
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
