package ca.pandaaa.animalquest.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String applyFormat(String message) {
        if (message == null)
            return "";
        message = message.replace(">>", "»").replace("<<", "«");

        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
            String before = message.substring(0, matcher.start());
            String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getAnimalQuestName() {
        return applyFormat(
                "&#16A4A4&lA&#23B6B6&ln&#2FC8C8&li&#3CDBDB&lm&#48EDED&la&#55FFFF&ll&#555555&lQ&#6a6a6a&lu&#7f7f7f&le&#949494&ls&#aaaaaa&lt");
    }

    public static boolean isDoubleOrInteger(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            return isInteger(string);
        }
        return true;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}
