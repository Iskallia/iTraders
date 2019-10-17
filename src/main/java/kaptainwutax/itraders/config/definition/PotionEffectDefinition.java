package kaptainwutax.itraders.config.definition;

import com.google.gson.annotations.Expose;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

public class PotionEffectDefinition {

    @Expose
    protected String name;

    @Expose
    protected int minAmplifier;

    @Expose
    protected int maxAmplifier;

    public PotionEffectDefinition(Potion potion, int minAmplifier, int maxAmplifier) {
        this(potion.getRegistryName().toString(), minAmplifier, maxAmplifier);
    }

    public PotionEffectDefinition(String name, int minAmplifier, int maxAmplifier) {
        this.name = name;
        this.minAmplifier = minAmplifier;
        this.maxAmplifier = maxAmplifier;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmplifier() {
        return maxAmplifier;
    }

    public int getMinAmplifier() {
        return minAmplifier;
    }

}
