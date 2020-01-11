package iskallia.itraders.util;

import java.text.NumberFormat;
import java.util.Locale;

public class Number2String {

    public static String withCommas(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String convert(int number) {
        if (number >= 1_000_000_000)
            return (number / 1_000_000_000) + " billion";

        if (number >= 1_000_000)
            return (number / 1_000_000) + " million";

        if (number >= 1_000)
            return (number / 1_000) + "K";

        return String.valueOf(number);
    }

}
