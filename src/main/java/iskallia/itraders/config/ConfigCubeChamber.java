package iskallia.itraders.config;

import com.google.gson.annotations.Expose;

public class ConfigCubeChamber extends Config {

    @Expose
    public int CAPACITY;

    @Expose
    public int ENERGY_MAX_INPUT;

    @Expose
    public int INFUSION_ENERGY_CONSUMPTION_PER_TICK;

    @Expose
    public int INFUSION_TICKS;

    @Override
    public String getLocation() {
        return "cubeChamber.json";
    }

    @Override
    protected void resetConfig() {
        CAPACITY = 10_000;
        ENERGY_MAX_INPUT = 25;
        INFUSION_ENERGY_CONSUMPTION_PER_TICK = 100;
        INFUSION_TICKS = 20 * 60 * 3;
    }

}
