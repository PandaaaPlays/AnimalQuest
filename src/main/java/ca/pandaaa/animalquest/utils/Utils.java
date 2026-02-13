package ca.pandaaa.animalquest.utils;

import ca.pandaaa.animalquest.enums.AnimalRank;
import ca.pandaaa.animalquest.enums.StaffRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
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

    public static String getSentenceCase(String string) {
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1).replaceAll("_", " ");
    }

    public static String getAnimalQuestName() {
        return applyFormat("&#16A4A4&lA&#23B6B6&ln&#2FC8C8&li&#3CDBDB&lm&#48EDED&la&#55FFFF&ll&#555555&lQ&#6a6a6a&lu&#7f7f7f&le&#949494&ls&#aaaaaa&lt");
    }

    public static String getRankPrefix(Player player) {
        StaffRank staffRank = StaffRank.getPlayerRank(player);
        if (staffRank != null) {
            return Utils.applyFormat(staffRank.getDisplayName() + " &f");
        } else {
            AnimalRank animalRank = AnimalRank.getPlayerRank(player);
            return animalRank != null ? Utils.applyFormat(animalRank.getDisplayName() + " &f") : "";
        }
    }
}
