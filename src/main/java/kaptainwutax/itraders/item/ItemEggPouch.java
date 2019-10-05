package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.gui.GuiHandler;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.world.data.DataEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemEggPouch extends Item {

    public ItemEggPouch(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack pouchItemStack = player.getHeldItem(hand);

        if(world.isRemote) {
            player.openGui(Traders.getInstance(), GuiHandler.POUCH, world, 0, 0, 0);
        }
        
        return super.onItemRightClick(world, player, hand);
    }

}