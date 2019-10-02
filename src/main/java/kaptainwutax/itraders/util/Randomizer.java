package kaptainwutax.itraders.util;

import java.util.Random;

public class Randomizer {

    private static final Random RANDOM = new Random(); // Seed with nanoTime()

    /**
     * Takes in a percentage, then returns a boolean with that percentage
     *
     * @param percentage Percentage between [0.0, 1.0]
     * @return True with given chance, false otherwise
     */
    public static boolean booleanWithPercentage(double percentage) {
        if (percentage < 0d || percentage > 1d)
            throw new IllegalArgumentException("Percentage value must be between [0.0, 1.0]");

        if (percentage == 1d)
            return true;

        return RANDOM.nextDouble() <= percentage;
    }

}
