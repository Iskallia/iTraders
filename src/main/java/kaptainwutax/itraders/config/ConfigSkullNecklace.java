package kaptainwutax.itraders.config;

import com.google.gson.annotations.Expose;
import net.minecraft.init.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class ConfigSkullNecklace extends Config {

    @Expose
    public List<String> POSITIVE_EFFECT_TABLE = new ArrayList<>();

    @Expose
    public List<String> NEGATIVE_EFFECT_TABLE = new ArrayList<>();

    @Override
    public String getLocation() {
        return "skullNecklace.json";
    }

    @Override
    protected void resetConfig() {
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.HASTE.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.HEALTH_BOOST.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.ABSORPTION.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.FIRE_RESISTANCE.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.REGENERATION.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.STRENGTH.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.SPEED.getRegistryName().toString());
        this.POSITIVE_EFFECT_TABLE.add(MobEffects.WATER_BREATHING.getRegistryName().toString());

        this.NEGATIVE_EFFECT_TABLE.add(MobEffects.MINING_FATIGUE.getRegistryName().toString());
        this.NEGATIVE_EFFECT_TABLE.add(MobEffects.BLINDNESS.getRegistryName().toString());
        this.NEGATIVE_EFFECT_TABLE.add(MobEffects.WEAKNESS.getRegistryName().toString());
        this.NEGATIVE_EFFECT_TABLE.add(MobEffects.SLOWNESS.getRegistryName().toString());
    }

}
