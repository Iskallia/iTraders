package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.card.SubCardData;
import kaptainwutax.itraders.card.SubCardGenerator;
import kaptainwutax.itraders.card.SubCardRarity;
import kaptainwutax.itraders.card.damage.SubCardDamage;
import kaptainwutax.itraders.card.damage.SubCardDamageType;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.util.NBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Class: ItemSubCard
 * Created by HellFirePvP
 * Date: 16.10.2019 / 20:06
 */
public class ItemSubCard extends Item {

    public ItemSubCard(String name) {
        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setMaxStackSize(1);
    }

    //TODO remove after test
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && stack.getItem() instanceof ItemSubCard && getCardData(stack) == null) {
            Tuple<SubCardRarity, SubCardData> generated = SubCardGenerator.generateRandom();
            setCardData(stack, generated.getSecond());
            setCardRarity(stack, generated.getFirst());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        SubCardRarity rarity = getCardRarity(stack);
        String description = I18n.format(rarity.getUnlocalizedName());
        if (!description.isEmpty()) {
            tooltip.add(rarity.getRarityColor() + description);
            tooltip.add("");
        }

        SubCardData data = getCardData(stack);
        if (data != null) {
            data.fillTooltip(tooltip::add);
        }
    }

    @Nullable
    public static SubCardData getCardData(@Nonnull ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return null;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        if (!base.hasKey("cardData", Constants.NBT.TAG_COMPOUND)) {
            return null;
        }
        return new SubCardData(base.getCompoundTag("cardData"));
    }

    public static void setCardData(@Nonnull ItemStack stack, @Nonnull SubCardData cardData) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        NBTTagCompound tag = new NBTTagCompound();
        cardData.writeToNBT(tag);
        base.setTag("cardData", tag);
    }

    @Nonnull
    public static SubCardRarity getCardRarity(@Nonnull ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return SubCardRarity.COMMON;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        return SubCardRarity.values()[MathHelper.clamp(base.getInteger("rarity"), 0, SubCardRarity.values().length - 1)];
    }

    public static void setCardRarity(@Nonnull ItemStack stack, @Nonnull SubCardRarity rarity) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        base.setInteger("rarity", rarity.ordinal());
    }

    @Nullable
    public static UUID getSubUUID(@Nonnull ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return null;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        return base.getUniqueId("playerUUID");
    }

    public static void setSubUUID(@Nonnull ItemStack stack, @Nonnull UUID playerUUID) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        base.setUniqueId("playerUUID", playerUUID);
    }

    @Nullable
    public static String getSubName(@Nonnull ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return null;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        return base.getString("playerName");
    }

    public static void setSubUUID(@Nonnull ItemStack stack, @Nonnull String playerName) {
        if (!(stack.getItem() instanceof ItemSubCard)) {
            return;
        }
        NBTTagCompound base = NBTHelper.getPersistentData(stack);
        base.setString("playerName", playerName);
    }

}
