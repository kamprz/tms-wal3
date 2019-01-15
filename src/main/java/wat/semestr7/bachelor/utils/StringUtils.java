package wat.semestr7.bachelor.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringUtils {
    private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
    public static String rateFormat(double value, int numberOfDigitsAfter)
    {
        return String.format("%."+numberOfDigitsAfter+"f",value).replace(",",".");
    }

    public static String amountFormat(BigDecimal amount)
    {
        return formatter.format(amount).replace(',','.');
    }
}
