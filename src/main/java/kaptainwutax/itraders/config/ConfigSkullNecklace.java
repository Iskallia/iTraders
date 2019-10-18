package kaptainwutax.itraders.config;

import com.google.gson.annotations.Expose;
import kaptainwutax.itraders.config.definition.PotionEffectDefinition;
import kaptainwutax.itraders.config.definition.RangeDefinition;
import kaptainwutax.itraders.util.Randomizer;
import net.minecraft.init.MobEffects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigSkullNecklace extends Config {

    @Expose
    public List<PotionEffectDefinition> EFFECT_TABLE = new ArrayList<>();

    @Expose
    public float NECKLACE_CREATION_RATE;

    @Expose
    public RangeDefinition NECKLACE_EFFECT_COUNT;

    @Expose
    public float GHOST_RENDER_OPACITY;

    @Override
    public String getLocation() {
        return "skullNecklace.json";
    }

    @Override
    protected void resetConfig() {
        this.NECKLACE_CREATION_RATE = 0.01f;
        this.NECKLACE_EFFECT_COUNT = new RangeDefinition(1, 2);

        this.GHOST_RENDER_OPACITY = 0.75f;

        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.HASTE, 0, 1));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.HEALTH_BOOST, 0, 9));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.ABSORPTION, 0, 9));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.FIRE_RESISTANCE, 0, 0));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.REGENERATION, 0, 3));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.STRENGTH, 0, 9));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.SPEED, 0, 9));
        this.EFFECT_TABLE.add(new PotionEffectDefinition(MobEffects.LUCK, 0, 0));
    }

    public List<PotionEffectDefinition> getRandomEffect(int amount) {
        List<PotionEffectDefinition> cloneList = new LinkedList<>(EFFECT_TABLE);
        List<PotionEffectDefinition> random = new LinkedList<>();

        for (int i = 0; i < amount; i++) {
            int index = Randomizer.randomIntEx(cloneList.size());
            random.add(cloneList.remove(index));
        }

        return random;
    }

}
