package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBooster extends Item {
	private int tier;
	
	public ItemBooster(String name, int tier) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		
		this.tier = tier;
	}
	
	/* ------------------------------- */
	
	public static double getSuccessRate(ItemStack boosterStack) {
		if (!(boosterStack.getItem() instanceof ItemBooster))
			return 0.01; // 1% chance on any item other than Booster. (Including EMPTY)
		
		int tier = ((ItemBooster) boosterStack.getItem()).tier;
		
		switch (tier) {
		case 1:
			return 0.10;
		case 2:
			return 0.25;
		case 3:
			return 0.50;
		case 4:
			return 0.75;
		case 5:
			return 0.90;
		default:
			return 0.01; // Probably malformed Booster item :c
		}
	}
}
