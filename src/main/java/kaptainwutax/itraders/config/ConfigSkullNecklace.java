package kaptainwutax.itraders.config;

import com.google.gson.annotations.Expose;
import kaptainwutax.itraders.config.definition.PotionEffectDefinition;
import kaptainwutax.itraders.util.Randomizer;
import net.minecraft.init.MobEffects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ConfigSkullNecklace extends Config {

    @Expose
    public List<PotionEffectDefinition> POSITIVE_EFFECT_TABLE = new ArrayList<>();

    @Expose
    public List<PotionEffectDefinition> NEGATIVE_EFFECT_TABLE = new ArrayList<>();

    @Override
    public String getLocation() {
        return "skullNecklace.json";
    }

    @Override
    protected void resetConfig() {
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.HASTE, 0, 1));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.HEALTH_BOOST, 0, 9));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.ABSORPTION, 0, 9));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.FIRE_RESISTANCE, 0, 0));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.REGENERATION, 0, 3));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.STRENGTH, 0, 9));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.SPEED, 0, 9));
        this.POSITIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.LUCK, 0, 0));

        this.NEGATIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.MINING_FATIGUE, 0, 9));
        this.NEGATIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.BLINDNESS, 0, 0));
        this.NEGATIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.WEAKNESS, 0, 9));
        this.NEGATIVE_EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.SLOWNESS, 0, 9));
    }

    public List<PotionEffectDefinition> getRandomPositive(int amount) {
        return getRandomFrom(POSITIVE_EFFECT_TABLE, amount);
    }

    public List<PotionEffectDefinition> getRandomNegative(int amount) {
        return getRandomFrom(NEGATIVE_EFFECT_TABLE, amount);
    }

    private List<PotionEffectDefinition> getRandomFrom(List<PotionEffectDefinition> list, int amount) {
        List<PotionEffectDefinition> cloneList = new LinkedList<>(list);
        List<PotionEffectDefinition> random = new LinkedList<>();

        for (int i = 0; i < amount; i++) {
            int index = Randomizer.randomIntEx(cloneList.size());
            random.add(cloneList.remove(index));
        }

        return random;
    }

}
