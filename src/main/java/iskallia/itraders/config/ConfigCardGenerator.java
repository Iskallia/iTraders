package iskallia.itraders.config;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import iskallia.itraders.card.damage.SubCardDamageType;
import iskallia.itraders.util.math.ValueRangeFloat;
import iskallia.itraders.util.math.ValueRangeInt;

public class ConfigCardGenerator extends Config {

    @Expose
    public float RARITY_CHANCE_RARE;
    @Expose
    public float RARITY_CHANCE_LEGENDARY;

    @Expose
    public float RARITY_RARE_BONUS;
    @Expose
    public float RARITY_LEGENDARY_BONUS;

    @Expose
    public ValueRangeInt HEALTH_RANGE;
    @Expose
    public ValueRangeInt ARMOR_RANGE;
    @Expose
    public ValueRangeFloat MOVESPEED_RANGE;

    @Expose
    public Map<SubCardDamageType, ConfiguredDamageRange> DAMAGE_RANGES;

    @Override
    public String getLocation() {
        return "card_generator.json";
    }

    @Override
    protected void resetConfig() {
        RARITY_CHANCE_RARE = 0.15F;
        RARITY_CHANCE_LEGENDARY = 0.04F;

        RARITY_RARE_BONUS = 0.1F;
        RARITY_LEGENDARY_BONUS = 0.2F;

        HEALTH_RANGE = new ValueRangeInt(80, 110);
        ARMOR_RANGE = new ValueRangeInt(10, 50);
        MOVESPEED_RANGE = new ValueRangeFloat(0.6F, 0.9F);

        DAMAGE_RANGES = new HashMap<>();
        for (SubCardDamageType type : SubCardDamageType.values()) {
            ValueRangeInt minRange = new ValueRangeInt(20, 25);
            ValueRangeInt maxRange = new ValueRangeInt(26, 40);
            DAMAGE_RANGES.put(type, new ConfiguredDamageRange(minRange, maxRange));
        }
    }

    public static class ConfiguredDamageRange {

        @Expose
        private ValueRangeInt MIN_RANGE;
        @Expose
        private ValueRangeInt MAX_RANGE;

        public ConfiguredDamageRange(ValueRangeInt MIN_RANGE, ValueRangeInt MAX_RANGE) {
            this.MIN_RANGE = MIN_RANGE;
            this.MAX_RANGE = MAX_RANGE;
        }

        public ValueRangeInt getMinRange() {
            return MIN_RANGE;
        }

        public ValueRangeInt getMaxRange() {
            return MAX_RANGE;
        }
    }

}
