package kaptainwutax.itraders.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemBaublesTest extends Item implements IBauble {

    public ItemBaublesTest() {
        this.setUnlocalizedName("item_baubles_test");
        this.setRegistryName(Traders.getResource("item_baubles_test"));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
        System.out.printf("%s - WORN\n", FMLCommonHandler.instance().getEffectiveSide());
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        System.out.printf("%s - EQUIPPED\n", FMLCommonHandler.instance().getEffectiveSide());
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        System.out.printf("%s - UNEQUIP\n", FMLCommonHandler.instance().getEffectiveSide());
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

}
