package kaptainwutax.itraders.util.math;

import java.util.Random;

import com.google.gson.annotations.Expose;

/**
 * Class: ValueRangeFloat
 * Created by HellFirePvP
 * Date: 17.10.2019 / 20:48
 */
public class ValueRangeFloat {

    @Expose
    private float MIN;
    @Expose
    private float MAX;

    public ValueRangeFloat(float min, float max) {
        this.MIN = min;
        this.MAX = max;
    }

    public float getMin() {
        return MIN;
    }

    public float getMax() {
        return MAX;
    }

    public float getValue(Random random) {
        if (MAX <= MIN) {
            return MIN;
        }
        return MIN + random.nextFloat() * (MAX - MIN);
    }
}
