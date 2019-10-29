package iskallia.itraders.config;

import com.google.gson.annotations.Expose;

public class ConfigCryoChamber extends Config {

    @Expose
    public int SHRINKING_TICKS;

    @Expose
    public int MIN_TICKS_BEFORE_FAIL;

    @Expose
    public double MIN_SHRINKING_SCALE;

    @Expose
    public double MAX_SHRINKING_SCALE;

    @Override
    public String getLocation() {
        return "cryoChamber.json";
    }

    @Override
    protected void resetConfig() {
        this.SHRINKING_TICKS = 20 * 60; // 1 minute

        this.MIN_TICKS_BEFORE_FAIL = 20 * 20; // 20 seconds

        this.MAX_SHRINKING_SCALE = 0.075;
        this.MIN_SHRINKING_SCALE = 0.010;
    }

}
