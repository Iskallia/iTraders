package iskallia.itraders.card;

import net.minecraft.util.text.TextFormatting;

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
