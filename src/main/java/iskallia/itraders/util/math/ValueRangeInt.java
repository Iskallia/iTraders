package iskallia.itraders.util.math;

import java.util.Random;

import com.google.gson.annotations.Expose;

/**
 * Class: ValueRangeInt
 * Created by HellFirePvP
 * Date: 17.10.2019 / 20:47
 */
public class ValueRangeInt {

    @Expose
    private int MIN;
    @Expose
    private int MAX;

    public ValueRangeInt(int min, int max) {
        this.MIN = min;
        this.MAX = max;
    }

    public int getMin() {
        return MIN;
    }

    public int getMax() {
        return MAX;
    }

    public int getValue(Random random) {
        if (MAX <= MIN) {
            return MIN;
        }
        return MIN + random.nextInt(MAX - MIN);
    }
}
