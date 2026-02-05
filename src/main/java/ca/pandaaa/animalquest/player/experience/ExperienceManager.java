package ca.pandaaa.animalquest.player.experience;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExperienceManager {
    private final AnimalQuest plugin;
    private final Map<Integer, Double> levelGoals = new HashMap<>();

    public ExperienceManager(AnimalQuest plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
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
}
