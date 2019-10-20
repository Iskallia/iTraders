package iskallia.itraders.util;

import javax.annotation.Nonnull;

import iskallia.itraders.Traders;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class: NBTHelper
 * Created by HellFirePvP
 * Date: 16.10.2019 / 20:09
 */
public class NBTHelper {

    @Nonnull
    public static NBTTagCompound getPersistentData(ItemStack item) {
        return getPersistentData(getData(item));
    }

    @Nonnull
    public static NBTTagCompound getPersistentData(NBTTagCompound base) {
        NBTTagCompound compound;
        if (hasPersistentData(base)) {
            compound = base.getCompoundTag(Traders.MOD_ID);
        } else {
            compound = new NBTTagCompound();
            base.setTag(Traders.MOD_ID, compound);
        }
        return compound;
    }

    public static boolean hasPersistentData(NBTTagCompound base) {
        return base.hasKey(Traders.MOD_ID) && base.getTag(Traders.MOD_ID) instanceof NBTTagCompound;
    }

    @Nonnull
    public static NBTTagCompound getData(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        return compound;
    }

}
