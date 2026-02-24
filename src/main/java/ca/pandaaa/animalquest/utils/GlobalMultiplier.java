package ca.pandaaa.animalquest.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.pandaaa.animalquest.enums.AnimalRank;

public class GlobalMultiplier {
    public static double getGlobalMultiplier() {
        double multiplier = 1.0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            multiplier += getPlayerMultiplier(player);
        }
        return multiplier;
    }

    public static double getPlayerMultiplier(Player player) {
        AnimalRank rank = AnimalRank.getPlayerRank(player);
        if (rank != null) {
            return rank.getLevel() * 0.01;
        }
        return 0D;
    }
}
