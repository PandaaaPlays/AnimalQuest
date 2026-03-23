package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.commands.*;
import ca.pandaaa.animalquest.managers.*;
import ca.pandaaa.animalquest.listeners.PlayerConnectionListener;
import ca.pandaaa.animalquest.listeners.PlayerGameplayListener;
import ca.pandaaa.animalquest.listeners.PlayerProtectionListener;
import ca.pandaaa.animalquest.listeners.QuestListener;
import ca.pandaaa.animalquest.listeners.BackupWorldListener;
import ca.pandaaa.animalquest.player.Mount;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.quests.QuestProgress;
import ca.pandaaa.animalquest.spells.*;
import ca.pandaaa.animalquest.listeners.ChatListener;
import ca.pandaaa.animalquest.listeners.CustomMobListener;
import ca.pandaaa.animalquest.listeners.JobListener;
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
    private QuestManager questManager;
    private HomeManager homeManager;
    private MountManager mountManager;
    private ZoneManager zoneManager;
    private WorldResetManager worldResetManager;
    private NPCManager npcManager;
    private CustomMobManager customMobManager;
    private BlockRegenManager blockRegenManager;
    private ActionBarManager actionBarManager;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterSerializers();

        // World Reset
        worldResetManager = new WorldResetManager(this);

        // Staff
        staffManager = new StaffManager();
        vanishManager = new VanishManager(this);

        // Experience / Level
        experienceManager = new ExperienceManager();
        experienceManager.loadConfig(this);

        // Action Bar
        actionBarManager = new ActionBarManager(this);

        // Jobs
        jobsManager = new JobsManager(this);
        guildManager = new GuildManager(this);

        // Player
        playerDataManager = new PlayerDataManager(this);
        scoreboardManager = new ScoreboardManager(this, playerDataManager);
        playerDataManager.initialize();

        // Quests
        questManager = new QuestManager(this);
        questManager.startQuestDisplayTask();

        // Spells
        spellManager = new SpellManager(playerDataManager);

        // Shops
        shopManager = new ShopManager(this);

        // Home
        homeManager = new HomeManager(playerDataManager);

        // Mounts
        mountManager = new MountManager(playerDataManager);

        // Ability
        new AbilityManager(this);

        // Zones
        zoneManager = new ZoneManager(this);

        // NPCs
        npcManager = new NPCManager(this);

        // Custom Mobs
        customMobManager = new CustomMobManager(this);

        // Block Regen
        blockRegenManager = new BlockRegenManager(this);

        RegisterEvents();
        RegisterCommands();

    }

    @Override
    public void onDisable() {
        if (worldResetManager != null)
            worldResetManager.shutdown();
        if (blockRegenManager != null)
            blockRegenManager.restoreAll();
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

    public QuestManager getQuestManager() {
        return questManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public MountManager getMountManager() {
        return mountManager;
    }

    public ZoneManager getZoneManager() {
        return zoneManager;
    }

    public WorldResetManager getWorldResetManager() {
        return worldResetManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public CustomMobManager getCustomMobManager() {
        return customMobManager;
    }

    public BlockRegenManager getBlockRegenManager() {
        return blockRegenManager;
    }

    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    private void RegisterSerializers() {
        ConfigurationSerialization.registerClass(PlayerData.class);
        ConfigurationSerialization.registerClass(Mount.class);
        ConfigurationSerialization.registerClass(Guild.class);
        ConfigurationSerialization.registerClass(QuestProgress.class);
    }

    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(playerDataManager, scoreboardManager),
                this);
        Bukkit.getPluginManager().registerEvents(new PlayerProtectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerGameplayListener(playerDataManager, scoreboardManager),
                this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(playerDataManager, staffManager), this);
        Bukkit.getPluginManager().registerEvents(new SpellListener(spellManager), this);
        Bukkit.getPluginManager().registerEvents(new QuestListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JobListener(playerDataManager), this);
        Bukkit.getPluginManager().registerEvents(new BackupWorldListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomMobListener(this), this);
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

        PluginCommand questCommand = getCommand("quests");
        if (questCommand != null) {
            QuestCommand questExecutor = new QuestCommand();
            questCommand.setExecutor(questExecutor);
            questCommand.setTabCompleter(questExecutor);
        }

        PluginCommand menuCommand = getCommand("menu");
        if (menuCommand != null) {
            menuCommand.setExecutor(new MenuCommand());
        }

        PluginCommand worldResetCommand = getCommand("worldreset");
        if (worldResetCommand != null) {
            WorldResetCommand worldResetExecutor = new WorldResetCommand();
            worldResetCommand.setExecutor(worldResetExecutor);
            worldResetCommand.setTabCompleter(worldResetExecutor);
        }
    }
}
