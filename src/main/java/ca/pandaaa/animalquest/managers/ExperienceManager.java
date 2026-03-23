package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExperienceManager {
    private final Map<Integer, Double> levelGoals = new HashMap<>();

    public void loadConfig(AnimalQuest plugin) {
        File configFile = new File(plugin.getDataFolder(), "levels-experience.yml");
        if (!configFile.exists()) {
            plugin.saveResource("levels-experience.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        levelGoals.clear();
        if (config.contains("levels")) {
            for (String key : config.getConfigurationSection("levels").getKeys(false)) {
                try {
                    int level = Integer.parseInt(key);
                    double goal = config.getDouble("levels." + key);
                    levelGoals.put(level, goal);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    public double getGoalForLevel(int level) {
        return levelGoals.getOrDefault(level, -1D);
    }

    public void sendLevelUpMessage(Player player, int newLevel) {
        player.sendMessage(
                Utils.applyFormat("&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(Utils.applyFormat("  &3&lPLAYER LEVEL UP!"));
        player.sendMessage(
                Utils.applyFormat("  &fYou reached level &b" + newLevel + "&f!"));
        player.sendMessage(
                Utils.applyFormat("&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));

        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.2f);
        player.spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation().add(0, 1, 0), 30, 0.5, 1.0, 0.5, 0.1);
    }
}
