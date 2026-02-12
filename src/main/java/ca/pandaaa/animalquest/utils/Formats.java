package ca.pandaaa.animalquest.utils;

import java.text.DecimalFormat;

public class Formats {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat EXPERIENCE_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.0");
    private static final DecimalFormat BONUS_FORMAT = new DecimalFormat("0.0");

    public static String formatMoney(double amount) {
        return MONEY_FORMAT.format(amount);
    }

    public static String formatExperienceScoreboard(double amount) {
        return EXPERIENCE_FORMAT.format(amount);
    }

    public static String formatPercentage(double amount) {
        return PERCENT_FORMAT.format(amount);
    }

    public static String formatBonus(double amount) {
        return BONUS_FORMAT.format(amount);
    }
}
