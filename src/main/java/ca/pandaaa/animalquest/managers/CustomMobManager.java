package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMobManager {
    public record CustomMobReward(int experience, List<String> messages) {
    }

    private final AnimalQuest plugin;
    private final Map<String, CustomMobReward> rewards = new HashMap<>();

    public CustomMobManager(AnimalQuest plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        File file = new File(plugin.getDataFolder(), "custom-mobs.yml");
        if (!file.exists()) {
            plugin.saveResource("custom-mobs.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        rewards.clear();
        if (config.contains("mobs")) {
            for (String key : config.getConfigurationSection("mobs").getKeys(false)) {
                int exp = config.getInt("mobs." + key + ".experience", 0);
                List<String> messages = config.getStringList("mobs." + key + ".messages");
                rewards.put(key.toLowerCase(), new CustomMobReward(exp, messages));
            }
        }
    }

    public CustomMobReward getReward(String mobId) {
        return rewards.get(mobId.toLowerCase());
    }
}
