package kaptainwutax.itraders.config.definition;

import com.google.gson.annotations.Expose;

import net.minecraft.potion.Potion;

public class PotionEffectDefinition {

    @Expose
    protected String name;

    @Expose
    protected RangeDefinition amplifier;

    public PotionEffectDefinition(Potion potion, int minAmplifier, int maxAmplifier) {
        this(potion.getRegistryName().toString(), minAmplifier, maxAmplifier);
    }

    public PotionEffectDefinition(String name, int minAmplifier, int maxAmplifier) {
        this.name = name;
        this.amplifier = new RangeDefinition(minAmplifier, maxAmplifier);
    }

    public String getName() {
        return name;
    }

    public RangeDefinition getAmplifier() {
        return amplifier;
    }

}
