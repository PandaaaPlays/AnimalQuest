package ca.pandaaa.animalquest.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.pandaaa.animalquest.enums.AnimalRank;

public class GlobalMultiplier {
    public static double getGlobalMultiplier() {
        double multiplier = 1.0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            AnimalRank rank = AnimalRank.getPlayerRank(player);
            if (rank != null) {
                multiplier += (rank.getLevel() * 0.01);
            }
        }
        return multiplier;
    }
}
