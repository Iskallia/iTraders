package iskallia.itraders.util.math;

public class MathHelper {

    public static float map(float value, float s1, float e1, float s2, float e2) {
        return (value - s1) * (e2 - s2) / (e1 - s1) + s2;
    }

    public static double map(double value, double s1, double e1, double s2, double e2) {
        return (value - s1) * (e2 - s2) / (e1 - s1) + s2;
    }

}
