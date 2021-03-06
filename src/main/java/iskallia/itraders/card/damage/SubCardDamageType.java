package iskallia.itraders.card.damage;

import java.util.function.Function;

import iskallia.itraders.util.math.ValueRangeInt;
import net.minecraft.util.text.TextFormatting;

/**
 * Class: SubCardDamageType
 * Created by HellFirePvP
 * Date: 17.10.2019 / 17:09
 */
public enum SubCardDamageType {

    UNTYPED(TextFormatting.GOLD, (rangeIn) -> rangeIn);

    private final TextFormatting displayColor;
    private final Function<ValueRangeInt, ValueRangeInt> modifier;

    SubCardDamageType(TextFormatting displayColor, Function<ValueRangeInt, ValueRangeInt> modifier) {
        this.displayColor = displayColor;
        this.modifier = modifier;
    }

    public TextFormatting getDisplayColor() {
        return displayColor;
    }

    public ValueRangeInt modify(ValueRangeInt dmg) {
        return this.modifier.apply(dmg);
    }

    public String getUnlocalizedName() {
        return String.format("itraders.card.damage.%s.name", this.name().toLowerCase());
    }

}
