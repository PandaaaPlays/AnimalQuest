package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Quests;
import ca.pandaaa.animalquest.quests.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * Manages all quests in the plugin
 */
public class QuestManager {
    private final AnimalQuest plugin;
    private final Map<String, Quest> quests;
    private final File questsFile;

    public QuestManager(AnimalQuest plugin) {
        this.plugin = plugin;
        this.quests = new HashMap<>();
        this.questsFile = new File(plugin.getDataFolder(), "quests.yml");

        loadQuests();
    }

    /**
     * Load quests from configuration file
     */
    private void loadQuests() {
        if (!questsFile.exists()) {
            createDefaultQuests();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(questsFile);
        ConfigurationSection questsSection = config.getConfigurationSection("quests");

        if (questsSection == null) {
            plugin.getLogger().warning("No quests found in quests.yml");
            return;
        }

        for (String questId : questsSection.getKeys(false)) {
            ConfigurationSection questSection = questsSection.getConfigurationSection(questId);
            if (questSection != null) {
                Quest quest = loadQuest(questId, questSection);
                if (quest != null) {
                    quests.put(questId, quest);
                }
            }
        }

        plugin.getLogger().info("Loaded " + quests.size() + " quests");
    }

    /**
     * Load a single quest from configuration
     */
    private Quest loadQuest(String id, ConfigurationSection section) {
        try {
            String name = section.getString("name", id);
            String description = section.getString("description", "");
            Material icon = Material.valueOf(section.getString("icon", "BOOK"));
            int requiredLevel = section.getInt("required_level", 1);
            List<String> requiredQuests = section.getStringList("required_quests");

            // Load steps
            List<QuestStep> steps = new ArrayList<>();
            ConfigurationSection stepsSection = section.getConfigurationSection("steps");
            if (stepsSection != null) {
                for (String stepKey : stepsSection.getKeys(false)) {
                    ConfigurationSection stepSection = stepsSection.getConfigurationSection(stepKey);
                    if (stepSection != null) {
                        QuestStep step = loadQuestStep(stepSection);
                        if (step != null) {
                            steps.add(step);
                        }
                    }
                }
            }

            // Load rewards
            QuestReward reward = loadReward(section.getConfigurationSection("rewards"));

            return new Quest(id, name, description, steps, reward, icon, requiredLevel, requiredQuests);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load quest: " + id);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load a quest step from configuration
     */
    private QuestStep loadQuestStep(ConfigurationSection section) {
        String description = section.getString("description", "");
        String typeStr = section.getString("type", "KILL_ENTITY");
        int amount = section.getInt("amount", 1);
        String target = section.getString("target", "");

        QuestObjective.ObjectiveType type = QuestObjective.ObjectiveType.valueOf(typeStr);
        QuestObjective objective = new QuestObjective(type, amount, target);

        return new QuestStep(description, objective);
    }

    /**
     * Load quest rewards from configuration
     */
    private QuestReward loadReward(ConfigurationSection section) {
        if (section == null) {
            return new QuestReward(0, 0, new ArrayList<>());
        }

        int experience = section.getInt("experience", 0);
        int money = section.getInt("money", 0);

        List<ItemStack> items = new ArrayList<>();
        ConfigurationSection itemsSection = section.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    Material material = Material.valueOf(itemSection.getString("material", "STONE"));
                    int amount = itemSection.getInt("amount", 1);
                    items.add(new ItemStack(material, amount));
                }
            }
        }

        return new QuestReward(experience, money, items);
    }

    /**
     * Create default quests configuration
     */
    private void createDefaultQuests() {
        YamlConfiguration config = new YamlConfiguration();

        // Example quest 1: First Steps
        config.set("quests.first_steps.name", "First Steps");
        config.set("quests.first_steps.description", "Begin your adventure!");
        config.set("quests.first_steps.icon", "COMPASS");
        config.set("quests.first_steps.required_level", 1);

        config.set("quests.first_steps.steps.1.description", "Collect 10 Oak Logs");
        config.set("quests.first_steps.steps.1.type", "COLLECT_ITEM");
        config.set("quests.first_steps.steps.1.amount", 10);
        config.set("quests.first_steps.steps.1.target", "OAK_LOG");

        config.set("quests.first_steps.steps.2.description", "Craft a Wooden Pickaxe");
        config.set("quests.first_steps.steps.2.type", "CRAFT_ITEM");
        config.set("quests.first_steps.steps.2.amount", 1);
        config.set("quests.first_steps.steps.2.target", "WOODEN_PICKAXE");

        config.set("quests.first_steps.rewards.experience", 100);
        config.set("quests.first_steps.rewards.money", 50);
        config.set("quests.first_steps.rewards.items.1.material", "STONE_PICKAXE");
        config.set("quests.first_steps.rewards.items.1.amount", 1);

        // Example quest 2: Monster Hunter
        config.set("quests.monster_hunter.name", "Monster Hunter");
        config.set("quests.monster_hunter.description", "Prove your combat skills!");
        config.set("quests.monster_hunter.icon", "IRON_SWORD");
        config.set("quests.monster_hunter.required_level", 3);
        config.set("quests.monster_hunter.required_quests", Arrays.asList("first_steps"));

        config.set("quests.monster_hunter.steps.1.description", "Kill 5 Zombies");
        config.set("quests.monster_hunter.steps.1.type", "KILL_ENTITY");
        config.set("quests.monster_hunter.steps.1.amount", 5);
        config.set("quests.monster_hunter.steps.1.target", "ZOMBIE");

        config.set("quests.monster_hunter.steps.2.description", "Kill 3 Skeletons");
        config.set("quests.monster_hunter.steps.2.type", "KILL_ENTITY");
        config.set("quests.monster_hunter.steps.2.amount", 3);
        config.set("quests.monster_hunter.steps.2.target", "SKELETON");

        config.set("quests.monster_hunter.rewards.experience", 250);
        config.set("quests.monster_hunter.rewards.money", 100);
        config.set("quests.monster_hunter.rewards.items.1.material", "DIAMOND_SWORD");
        config.set("quests.monster_hunter.rewards.items.1.amount", 1);

        try {
            config.save(questsFile);
            plugin.getLogger().info("Created default quests.yml");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create default quests.yml");
            e.printStackTrace();
        }
    }

    /**
     * Get a quest by ID
     */
    public Quest getQuest(String questId) {
        return quests.get(questId);
    }

    /**
     * Get all quests
     */
    public Collection<Quest> getAllQuests() {
        return quests.values();
    }

    /**
     * Check if a player can unlock a quest
     */
    public boolean canUnlockQuest(Player player, Quest quest) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();

        // Check if already active or completed
        if (playerQuests.hasActiveQuest(quest.getId()) || playerQuests.hasCompletedQuest(quest.getId())) {
            return false;
        }

        // Check level requirement
        if (data.getExperience().getLevel() < quest.getRequiredLevel()) {
            return false;
        }

        // Check required quests
        for (String requiredQuestId : quest.getRequiredQuests()) {
            if (!playerQuests.hasCompletedQuest(requiredQuestId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Start a quest for a player
     */
    public boolean startQuest(Player player, String questId) {
        Quest quest = getQuest(questId);
        if (quest == null) {
            player.sendMessage("§cQuest not found!");
            return false;
        }

        if (!canUnlockQuest(player, quest)) {
            player.sendMessage("§cYou cannot start this quest yet!");
            return false;
        }

        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.getQuests().startQuest(questId);

        player.sendMessage("§a§lQuest Started: §f" + quest.getName());
        player.sendMessage("§7" + quest.getDescription());

        return true;
    }

    /**
     * Update quest progress for a player
     */
    public void updateQuestProgress(Player player, QuestObjective.ObjectiveType type, String target, int amount) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();

        for (Map.Entry<String, QuestProgress> entry : playerQuests.getActiveQuests().entrySet()) {
            Quest quest = getQuest(entry.getKey());
            if (quest == null)
                continue;

            QuestProgress progress = entry.getValue();
            QuestStep currentStep = quest.getStep(progress.getCurrentStep());

            if (currentStep == null)
                continue;

            QuestObjective objective = currentStep.getObjective();

            // Check if this update matches the current objective
            if (objective.getType() == type && objective.getTarget().equalsIgnoreCase(target)) {
                progress.addProgress(amount);

                // Check if step is complete
                if (currentStep.isComplete(progress.getCurrentProgress())) {
                    completeStep(player, quest, progress);
                } else {
                    // Update action bar with progress
                    updateQuestDisplay(player);
                }
            }
        }
    }

    /**
     * Complete a quest step
     */
    private void completeStep(Player player, Quest quest, QuestProgress progress) {
        int nextStep = progress.getCurrentStep() + 1;

        if (nextStep >= quest.getTotalSteps()) {
            // Quest complete!
            completeQuest(player, quest);
        } else {
            // Move to next step
            progress.setCurrentStep(nextStep);
            QuestStep nextStepObj = quest.getStep(nextStep);

            player.sendMessage("§a§lStep Complete!");
            player.sendMessage("§eNext: §f" + nextStepObj.getDescription());

            updateQuestDisplay(player);
        }
    }

    /**
     * Complete a quest
     */
    private void completeQuest(Player player, Quest quest) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.getQuests().completeQuest(quest.getId());

        player.sendMessage("§6§l§m━━━━━━━━━━━━━━━━━━━━━━━━━━");
        player.sendMessage("§a§lQUEST COMPLETE!");
        player.sendMessage("§e" + quest.getName());
        player.sendMessage("");

        // Give rewards
        quest.getReward().giveRewards(player);

        player.sendMessage("§6§l§m━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Play sound
        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }

    /**
     * Update the quest display above the player's hotbar
     */
    public void updateQuestDisplay(Player player) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        QuestProgress trackedProgress = data.getQuests().getTrackedQuest();

        if (trackedProgress == null) {
            return;
        }

        Quest quest = getQuest(trackedProgress.getQuestId());
        if (quest == null) {
            return;
        }

        QuestStep currentStep = quest.getStep(trackedProgress.getCurrentStep());
        if (currentStep == null) {
            return;
        }

        String progressStr = currentStep.getProgressString(trackedProgress.getCurrentProgress());
        String message = "§6" + quest.getName() + " §8[§e" + (trackedProgress.getCurrentStep() + 1) +
                "§7/§e" + quest.getTotalSteps() + "§8] §f" + currentStep.getDescription() +
                " §7(" + progressStr + ")";

        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                new net.md_5.bungee.api.chat.TextComponent(message));
    }

    /**
     * Start periodic quest display updates
     */
    public void startQuestDisplayTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateQuestDisplay(player);
            }
        }, 20L, 40L); // Update every 2 seconds
    }

    /**
     * Abandon a quest
     */
    public void abandonQuest(Player player, String questId) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.getQuests().abandonQuest(questId);

        Quest quest = getQuest(questId);
        if (quest != null) {
            player.sendMessage("§cAbandoned quest: §f" + quest.getName());
        }
    }
}
