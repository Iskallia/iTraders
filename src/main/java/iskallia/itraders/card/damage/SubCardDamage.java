package iskallia.itraders.card.damage;

import java.util.Random;
import java.util.function.Consumer;

import iskallia.itraders.util.math.ValueRangeInt;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubCardDamage {

    private static final Random rand = new Random();

    private final SubCardDamageType type;
    private final SubCardAttackType attackType;
    private final ValueRangeInt damageRange;

    public SubCardDamage(SubCardDamageType type, SubCardAttackType attackType, ValueRangeInt damageRange) {
        this.type = type;
        this.attackType = attackType;
        this.damageRange = type.modify(damageRange);
    }

    public SubCardDamage(NBTTagCompound in) {
        this.type = SubCardDamageType.values()[MathHelper.clamp(in.getInteger("type"), 0, SubCardDamageType.values().length - 1)];
        this.attackType = SubCardAttackType.values()[MathHelper.clamp(in.getInteger("attackType"), 0, SubCardAttackType.values().length - 1)];
        this.damageRange = new ValueRangeInt(in.getInteger("min"), in.getInteger("max"));
    }

    @SideOnly(Side.CLIENT)
    public void fillTooltip(Consumer<String> tooltip) {
        tooltip.accept(I18n.format("itraders.card.damage.format",
                this.type.getDisplayColor() + I18n.format(this.type.getUnlocalizedName()),
                TextFormatting.RESET.toString() + TextFormatting.BLUE.toString() + I18n.format("itraders.card.damage"),
                this.type.getDisplayColor().toString() + this.damageRange.getMin(),
                this.type.getDisplayColor().toString() + this.damageRange.getMax()));
    }

    public SubCardDamageType getDamageType() {
        return type;
    }

    public SubCardAttackType getAttackType() {
        return attackType;
    }

    public int getDamageRoll() {
        return this.damageRange.getValue(rand);
    }

    public void writeToNBT(NBTTagCompound out) {
        out.setInteger("type", this.type.ordinal());
        out.setInteger("attackType", this.attackType.ordinal());
        out.setInteger("min", this.damageRange.getMin());
        out.setInteger("max", this.damageRange.getMax());
    }

}
