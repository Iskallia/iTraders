package kaptainwutax.itraders.card;

import kaptainwutax.itraders.card.damage.SubCardDamage;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.function.Consumer;


/**
 * Class: SubCardData
 * Created by HellFirePvP
 * Date: 16.10.2019 / 19:45
 */
public class SubCardData {

    private static final DecimalFormat MS_FORMAT = new DecimalFormat("##.##");

    private final int health;
    private final int armor;
    private final float movespeed;
    private final SubCardDamage damage;

    public SubCardData(int health, int armor, float movespeed, SubCardDamage damage) {
        this.health = health;
        this.armor = armor;
        this.movespeed = movespeed;
        this.damage = damage;
    }

    public SubCardData(NBTTagCompound in) {
        this.health = in.getInteger("health");
        this.armor = in.getInteger("armor");
        this.movespeed = in.getFloat("movespeed");
        this.damage = new SubCardDamage(in.getCompoundTag("damage"));
    }

    @SideOnly(Side.CLIENT)
    public void fillTooltip(Consumer<String> tooltip) {
        tooltip.accept(I18n.format("itraders.card.attribute.format",
                TextFormatting.BLUE + I18n.format("itraders.card.attribute.health.name"),
                TextFormatting.GOLD.toString() + health));
        tooltip.accept(I18n.format("itraders.card.attribute.format",
                TextFormatting.BLUE + I18n.format("itraders.card.attribute.armor.name"),
                TextFormatting.GOLD.toString() + armor));
        tooltip.accept(I18n.format("itraders.card.attribute.format",
                TextFormatting.BLUE + I18n.format("itraders.card.attribute.movespeed.name"),
                TextFormatting.GOLD.toString() + MS_FORMAT.format(movespeed) + I18n.format("itraders.card.attribute.movespeed.type.name")));

        this.damage.fillTooltip(tooltip);
    }

    public void writeToNBT(NBTTagCompound out) {
        out.setInteger("health", this.health);
        out.setInteger("armor", this.armor);
        out.setFloat("movespeed", this.movespeed);
        NBTTagCompound tag = new NBTTagCompound();
        this.damage.writeToNBT(tag);
        out.setTag("damage", tag);
    }

}
