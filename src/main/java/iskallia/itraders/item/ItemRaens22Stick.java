package iskallia.itraders.item;

import iskallia.itraders.Traders;
import iskallia.itraders.entity.EntityTrader;
import iskallia.itraders.init.InitItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemRaens22Stick extends Item {

    public ItemRaens22Stick(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

}
