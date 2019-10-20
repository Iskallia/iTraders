package kaptainwutax.itraders.card;

import static kaptainwutax.itraders.init.InitConfig.CONFIG_CARD_GENERATOR;

import java.util.Random;

import kaptainwutax.itraders.card.damage.SubCardDamage;
import kaptainwutax.itraders.card.damage.SubCardDamageType;
import kaptainwutax.itraders.util.math.ValueRangeInt;
import net.minecraft.util.Tuple;

/**
 * Class: SubCardGenerator
 * Created by HellFirePvP
 * Date: 16.10.2019 / 20:55
 */
public class SubCardGenerator {

    private static final Random rand = new Random();

    public static Tuple<SubCardRarity, SubCardData> generateRandom() {
        SubCardRarity rarity = SubCardRarity.COMMON;
        float bonusMultiplier = 1F;
        if (rand.nextFloat() <= CONFIG_CARD_GENERATOR.RARITY_CHANCE_RARE) {
            rarity = SubCardRarity.RARE;
            bonusMultiplier += CONFIG_CARD_GENERATOR.RARITY_RARE_BONUS;
        } else if (rand.nextFloat() <= CONFIG_CARD_GENERATOR.RARITY_CHANCE_LEGENDARY) {
            rarity = SubCardRarity.LEGENDARY;
            bonusMultiplier += CONFIG_CARD_GENERATOR.RARITY_LEGENDARY_BONUS;
        }

        int health = Math.round(CONFIG_CARD_GENERATOR.HEALTH_RANGE.getValue(rand) * bonusMultiplier);
        int armor = Math.round(CONFIG_CARD_GENERATOR.ARMOR_RANGE.getValue(rand) * bonusMultiplier);
        float moveSpeed = CONFIG_CARD_GENERATOR.MOVESPEED_RANGE.getValue(rand) * bonusMultiplier;

        SubCardDamageType damageType = SubCardDamageType.UNTYPED; // reee
        ValueRangeInt minRange = CONFIG_CARD_GENERATOR.DAMAGE_RANGES.get(damageType).getMinRange();
        ValueRangeInt maxRange = CONFIG_CARD_GENERATOR.DAMAGE_RANGES.get(damageType).getMaxRange();
        int min = Math.round(minRange.getValue(rand) * bonusMultiplier);
        int max = Math.round(maxRange.getValue(rand) * bonusMultiplier);
        SubCardDamage damage = new SubCardDamage(damageType, new ValueRangeInt(min, max));

        SubCardData generated = new SubCardData(health, armor, moveSpeed, damage);
        return new Tuple<>(rarity, generated);
    }

}
