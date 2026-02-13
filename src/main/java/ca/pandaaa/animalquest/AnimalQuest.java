package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.commands.*;
import ca.pandaaa.animalquest.managers.GuildManager;
import ca.pandaaa.animalquest.managers.JobsManager;
import ca.pandaaa.animalquest.listeners.PlayerListener;
import ca.pandaaa.animalquest.managers.ScoreboardManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.managers.ExperienceManager;
import ca.pandaaa.animalquest.spells.*;
import ca.pandaaa.animalquest.managers.ShopManager;
import ca.pandaaa.animalquest.managers.StaffManager;
import ca.pandaaa.animalquest.managers.VanishManager;
import ca.pandaaa.animalquest.managers.AbilityManager;
import ca.pandaaa.animalquest.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnimalQuest extends JavaPlugin {

    private static AnimalQuest plugin;
    private ExperienceManager experienceManager;
    private PlayerDataManager playerDataManager;
    private JobsManager jobsManager;
    private SpellManager spellManager;
    private ScoreboardManager scoreboardManager;
    private ShopManager shopManager;
    private StaffManager staffManager;
    private VanishManager vanishManager;
    private GuildManager guildManager;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterSerializers();

        // Staff
        staffManager = new StaffManager();
        vanishManager = new VanishManager(this);

        // Shops
        shopManager = new ShopManager(this);

        // Experience / Level
        experienceManager = new ExperienceManager();
        experienceManager.loadConfig(this);

        // Player
        playerDataManager = new PlayerDataManager(this);
        scoreboardManager = new ScoreboardManager(this, playerDataManager);
        playerDataManager.initialize();

        // Jobs
        jobsManager = new JobsManager(this);
        guildManager = new GuildManager(this);

        // Spells
        spellManager = new SpellManager(playerDataManager);

        // Ability
        new AbilityManager(this);

        RegisterEvents();
        RegisterCommands();

    }

    @Override
    public void onDisable() {
        playerDataManager.shutdown();
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

    public JobsManager getJobsManager() {
        return jobsManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }

    private void RegisterSerializers() {
        ConfigurationSerialization.registerClass(PlayerData.class);
        ConfigurationSerialization.registerClass(Guild.class);
    }

    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(playerDataManager, scoreboardManager), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(playerDataManager, staffManager), this);
        Bukkit.getPluginManager().registerEvents(new SpellListener(spellManager), this);
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

        PluginCommand aptitudeCommand = getCommand("aptitudes");
        if (aptitudeCommand != null) {
            aptitudeCommand.setExecutor(new AptitudeCommand());
        }

        PluginCommand broadcastCommand = getCommand("broadcast");
        if (broadcastCommand != null) {
            broadcastCommand.setExecutor(new BroadcastCommand());
        }

        PluginCommand payCommand = getCommand("pay");
        if (payCommand != null) {
            PayCommand payExecutor = new PayCommand();
            payCommand.setExecutor(payExecutor);
            payCommand.setTabCompleter(payExecutor);
        }

        PluginCommand mountCommand = getCommand("mount");
        if (mountCommand != null) {
            mountCommand.setExecutor(new MountCommand());
        }

        PluginCommand guildCommand = getCommand("guild");
        if (guildCommand != null) {
            GuildCommand guildExecutor = new GuildCommand();
            guildCommand.setExecutor(guildExecutor);
            guildCommand.setTabCompleter(guildExecutor);
        }

        PluginCommand vanishCommand = getCommand("vanish");
        if (vanishCommand != null) {
            vanishCommand.setExecutor(new VanishCommand());
        }

        PluginCommand staffChatCommand = getCommand("staffchat");
        if (staffChatCommand != null) {
            staffChatCommand.setExecutor(new StaffChatCommand());
        }
    }
}
