package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.gui.GuiHandler;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEggPouch extends Item {

    public ItemEggPouch(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.openGui(Traders.getInstance(), GuiHandler.POUCH, world, 0, 0, 0);   
        return super.onItemRightClick(world, player, hand);
    }

}