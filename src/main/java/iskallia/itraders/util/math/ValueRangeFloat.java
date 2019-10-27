package iskallia.itraders.util.math;

import java.util.Random;

import com.google.gson.annotations.Expose;

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
