package kaptainwutax.itraders.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.util.RomanLiterals;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/*
 * NBT: {
 *     PotionEffects: [
 *          { name:"mining_fatigue", amplifier:5 },
 *          { name:"haste", amplifier:1 }
 *     ]
 * }
 */
public class ItemSkullNeck extends Item implements IBauble {

    public static List<PotionEffect> getPotionEffects(ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null || !stackNBT.hasKey("PotionEffects", 9)) // 9 - LIST
            return null;

        List<PotionEffect> potionEffectList = new LinkedList<>();

        NBTTagList potionEffects = stackNBT.getTagList("PotionEffects", 10);
        for (NBTBase potionEffect : potionEffects) {
            String name = ((NBTTagCompound) potionEffect).getString("name");
            int amplifier = Math.max(1, ((NBTTagCompound) potionEffect).getInteger("amplifier"));

            if (name.isEmpty())
                continue; // Malformed PotionEffects entry

            Potion potion = Potion.getPotionFromResourceLocation(name);

            if (potion == null)
                continue; // Unknown potion name

            potionEffectList.add(new PotionEffect(potion, Integer.MAX_VALUE, amplifier, false, false));
        }

        return potionEffectList;
    }

    public ItemSkullNeck(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) { }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            List<PotionEffect> potionEffects = getPotionEffects(itemstack);

            if (potionEffects == null) return;

            for (PotionEffect potionEffect : potionEffects) {
                player.addPotionEffect(potionEffect);
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (!player.world.isRemote) {
            List<PotionEffect> potionEffects = getPotionEffects(itemstack);

            if (potionEffects == null) return;

            for (PotionEffect potionEffect : potionEffects) {
                player.removePotionEffect(potionEffect.getPotion());
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        List<PotionEffect> potionEffects = getPotionEffects(stack);

        if (potionEffects == null) return;

        for (PotionEffect potionEffect : potionEffects) {
            String effectTranslationKey = potionEffect.getEffectName();

            TextFormatting color = potionEffect.getPotion().isBadEffect()
                    ? TextFormatting.RED
                    : TextFormatting.GREEN;

            String effectName = I18n.format(effectTranslationKey);
            String amplifier = RomanLiterals.translate(potionEffect.getAmplifier());

            tooltip.add(color + effectName + " " + amplifier);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
