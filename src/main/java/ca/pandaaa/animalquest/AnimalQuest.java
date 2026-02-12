package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.commands.Commands;
import ca.pandaaa.animalquest.commands.JobsCommand;
import ca.pandaaa.animalquest.jobs.JobsManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.PlayerDataManager;
import ca.pandaaa.animalquest.player.experience.ExperienceManager;
import ca.pandaaa.animalquest.spells.*;
import ca.pandaaa.animalquest.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnimalQuest extends JavaPlugin {

    private static AnimalQuest plugin;
    private PlayerDataManager playerDataManager;
    private ExperienceManager experienceManager;
    private JobsManager jobsManager;

    private SpellManager spellManager;
    private ScoreboardManager scoreboardManager;
    private ShopManager shopManager;
    private MultiplierManager multiplierManager;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterSerializers();

        experienceManager = new ExperienceManager(this);
        jobsManager = new JobsManager(this);

        playerDataManager = new PlayerDataManager(this);
        spellManager = new SpellManager(playerDataManager);
        scoreboardManager = new ScoreboardManager(playerDataManager);
        shopManager = new ShopManager();
        multiplierManager = new MultiplierManager();

        RegisterEvents();
        RegisterCommands();

        // TODO Not here...
        spellManager.registerSpell(new Charge());
        spellManager.registerSpell(new CraftsmansAnvil());
        spellManager.registerSpell(new Cyclone());
        spellManager.registerSpell(new DragonsStrike());
        spellManager.registerSpell(new Endurance());
        spellManager.registerSpell(new Fireball());
        spellManager.registerSpell(new FireShield());
        spellManager.registerSpell(new FireSpirit());
        spellManager.registerSpell(new FlowerShield());
        spellManager.registerSpell(new HealingSpree());
        spellManager.registerSpell(new Immortal());
        spellManager.registerSpell(new LightningSpeed());
        spellManager.registerSpell(new StoneShield());
        spellManager.registerSpell(new Strength());

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = playerDataManager.loadPlayer(player.getUniqueId());
            data.updateManaDisplay(player);

            // Delay setup slightly to ensure everything is initialized and avoid issues
            // during reload
            org.bukkit.Bukkit.getScheduler().runTaskLater(this, () -> {
                if (player.isOnline()) {
                    scoreboardManager.setupScoreboard(player);
                    scoreboardManager.updatePlayerTablistDisplay(player);
                    scoreboardManager.updateTablistHeader();
                }
            }, 1L);
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

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

    public MultiplierManager getMultiplierManager() {
        return multiplierManager;
    }

    private void RegisterSerializers() {
        ConfigurationSerialization.registerClass(PlayerData.class);
    }

    private void RegisterEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(playerDataManager, scoreboardManager), this);
        Bukkit.getPluginManager().registerEvents(new SpellListener(spellManager), this);
        Bukkit.getPluginManager().registerEvents(shopManager, this);
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
            aptitudeCommand.setExecutor(new ca.pandaaa.animalquest.commands.AptitudeCommand());
        }
    }
}
