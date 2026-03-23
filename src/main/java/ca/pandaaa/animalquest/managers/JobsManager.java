package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JobsManager {

    private final AnimalQuest plugin;
    private final Map<Integer, Double> levelGoals = new HashMap<>();

    public JobsManager(AnimalQuest plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "jobs-levels-experience.yml");
        if (!configFile.exists()) {
            plugin.saveResource("jobs-levels-experience.yml", false);
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

    public void sendJobLevelUpMessage(Player player, Job job, int newLevel) {
        player.sendMessage(Utils.applyFormat("&a&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(Utils.applyFormat("  &2&lJOB LEVEL UP! &a- &f" + Utils.getSentenceCase(job.name())));
        player.sendMessage(Utils.applyFormat("  &fYou are now level &a" + newLevel + "&f!"));
        player.sendMessage(Utils.applyFormat("&a&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));

        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.spawnParticle(org.bukkit.Particle.HEART, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);

        ca.pandaaa.animalquest.AnimalQuest.getPlugin().getScoreboardManager().updateScoreboard(player, false);
    }
}
