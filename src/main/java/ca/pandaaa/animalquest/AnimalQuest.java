package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.commands.Commands;
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
    private SpellManager spellManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        plugin = this;
        RegisterSerializers();

        experienceManager = new ExperienceManager(this);
        playerDataManager = new PlayerDataManager(this);
        spellManager = new SpellManager(playerDataManager);
        scoreboardManager = new ScoreboardManager(playerDataManager);

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
    }

    private void RegisterCommands() {
        Commands commandExecutor = new Commands();
        PluginCommand animalQuestCommand = getCommand("animalquest");
        if (animalQuestCommand != null) {
            animalQuestCommand.setExecutor(commandExecutor);
            animalQuestCommand.setTabCompleter(commandExecutor);
        }

        PluginCommand aptitudeCommand = getCommand("aptitudes");
        if (aptitudeCommand != null) {
            aptitudeCommand.setExecutor(new ca.pandaaa.animalquest.commands.AptitudeCommand());
        }
    }
}
