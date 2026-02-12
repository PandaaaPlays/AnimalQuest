package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.player.AnimalRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MultiplierManager {
    public MultiplierManager() {
    }

    public double getGlobalMultiplier() {
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
