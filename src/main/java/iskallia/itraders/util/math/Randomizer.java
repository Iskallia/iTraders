package iskallia.itraders.util.math;

import java.util.Random;

public class Randomizer {

    private static final Random RANDOM = new Random();

    /**
     * @return A random integer between [Integer.MIN_VALUE, Integer.MAX_VALUE]
     */
    public static int randomInt() {
        return RANDOM.nextInt();
    }

    /**
     * @return A random integer between [0, max)
     */
    public static int randomIntEx(int max) {
        return RANDOM.nextInt(max);
    }

    /**
     * @return A random integer between [min, max]
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * @return A random double between [0.0, 1.0)
     */
    public static double randomDouble() {
        return RANDOM.nextDouble();
    }

}
