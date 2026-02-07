package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.commands.Commands;
import ca.pandaaa.animalquest.commands.JobsCommand;
import ca.pandaaa.animalquest.jobs.JobsGUIListener;
import ca.pandaaa.animalquest.jobs.manager.JobExperienceManager;
import ca.pandaaa.animalquest.jobs.manager.JobLevelRewardManager;
import ca.pandaaa.animalquest.jobs.manager.JobsLevelExperienceManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.PlayerDataManager;
import ca.pandaaa.animalquest.player.experience.ExperienceManager;
import ca.pandaaa.animalquest.spells.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnimalQuest extends JavaPlugin {

    private static AnimalQuest plugin;
    private PlayerDataManager playerDataManager;
    private ExperienceManager experienceManager;
    private JobExperienceManager jobExperienceManager;
    private JobLevelRewardManager jobLevelRewardManager;
    private SpellManager spellManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterSerializers();

        experienceManager = new ExperienceManager(this);
        jobExperienceManager = new JobsLevelExperienceManager(this);
        jobLevelRewardManager = new JobLevelRewardManager();
        playerDataManager = new PlayerDataManager(this);
        spellManager = new SpellManager(playerDataManager);
        scoreboardManager = new ScoreboardManager(playerDataManager);

        RegisterEvents();
        RegisterCommands();

        // TODO Not here...
        spellManager.registerSpell(new HealSpell());
        spellManager.registerSpell(new SpeedSpell());

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = playerDataManager.loadPlayer(player.getUniqueId());
            data.updateManaDisplay(player);
            scoreboardManager.setupScoreboard(player);
        }
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }
    }

    public static AnimalQuest getPlugin() {
        return plugin;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ExperienceManager getExperienceManager() {
        return experienceManager;
    }

    public JobExperienceManager getJobExperienceManager() {
        return jobExperienceManager;
    }

    public JobLevelRewardManager getJobLevelRewardManager() {
        return jobLevelRewardManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    private void RegisterSerializers() {
        ConfigurationSerialization.registerClass(PlayerData.class);
    }

    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(playerDataManager, scoreboardManager), this);
        Bukkit.getPluginManager().registerEvents(new SpellListener(spellManager), this);
        Bukkit.getPluginManager().registerEvents(new JobsGUIListener(), this);
    }

    private void RegisterCommands() {
        Commands commandExecutor = new Commands();
        PluginCommand animalQuestCommand = getCommand("animalquest");
        if (animalQuestCommand != null) {
            animalQuestCommand.setExecutor(commandExecutor);
            animalQuestCommand.setTabCompleter(commandExecutor);
        }

        PluginCommand jobsCommand = getCommand("jobs");
        if (jobsCommand != null) {
            jobsCommand.setExecutor(new JobsCommand());
        }
    }
}

// TODO List:
/*
 * - Jobs system
 * - 4 jobs : Lumberjack, Miner, Alchemist, Explorer
 * - Must be able to see it with the /jobs in a GUI that is pretty.
 * - Each job has 20 levels. Each levels must give access to new stuff to the
 * player (such as dropping rare drops when lumberjack high and mining wood)
 * - Players start at level 1.
 * - Vanish system
 * -
 * - Guild system
 * - Spells
 * - Aptitudes GUI
 * - Shop system
 * -
 */
