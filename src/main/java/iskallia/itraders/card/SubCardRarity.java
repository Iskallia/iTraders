package iskallia.itraders.card;

import net.minecraft.util.text.TextFormatting;

/**
 * Class: SubCardRarity
 * Created by HellFirePvP
 * Date: 16.10.2019 / 20:45
 */
public enum SubCardRarity {

    COMMON(TextFormatting.WHITE),
    RARE(TextFormatting.GOLD),
    LEGENDARY(TextFormatting.RED);

    private final TextFormatting rarityColor;

    SubCardRarity(TextFormatting rarityColor) {
        this.rarityColor = rarityColor;
    }

    public TextFormatting getRarityColor() {
        return this.rarityColor;
    }

    public String getUnlocalizedName() {
        return String.format("itraders.card.rarity.%s.name", this.name().toLowerCase());
    }

}
