package iskallia.itraders.config.definition;

import com.google.gson.annotations.Expose;

public class RangeDefinition {

    @Expose
    protected int min;

    @Expose
    protected int max;

    public RangeDefinition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
