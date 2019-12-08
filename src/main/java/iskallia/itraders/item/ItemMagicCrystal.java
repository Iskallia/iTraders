package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitItem;
import net.minecraft.item.Item;

public class ItemMagicCrystal extends Item {

    public ItemMagicCrystal(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

}
