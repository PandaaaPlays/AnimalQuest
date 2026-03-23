package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Quests;
import ca.pandaaa.animalquest.quests.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

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

    private void loadQuests() {
        if (!questsFile.exists()) {
            plugin.saveResource("quests.yml", false);
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

    private Quest loadQuest(String id, ConfigurationSection section) {
        try {
            String name = section.getString("name", id);
            String description = section.getString("description", "");
            Material icon = Material.valueOf(section.getString("icon", "BOOK"));
            int requiredLevel = section.getInt("required_level", 1);
            List<String> requiredQuests = section.getStringList("required_quests");

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

            QuestReward reward = loadReward(section.getConfigurationSection("rewards"));

            return new Quest(id, name, description, steps, reward, icon, requiredLevel, requiredQuests);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load quest: " + id);
            e.printStackTrace();
            return null;
        }
    }

    private QuestStep loadQuestStep(ConfigurationSection section) {
        String description = section.getString("description", "");
        String typeStr = section.getString("type", "KILL_ENTITY");
        int amount = section.getInt("amount", 1);
        String target = section.getString("target", "");

        Location location = null;
        if (section.contains("location")) {
            String worldName = section.getString("location.world", "world");
            double x = section.getDouble("location.x", 0);
            double y = section.getDouble("location.y", 0);
            double z = section.getDouble("location.z", 0);
            location = new Location(Bukkit.getWorld(worldName), x, y, z);
        }

        QuestObjective.ObjectiveType type = QuestObjective.ObjectiveType.valueOf(typeStr);
        QuestObjective objective = new QuestObjective(type, amount, target, location);

        return new QuestStep(description, objective);
    }

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

    public Quest getQuest(String questId) {
        return quests.get(questId);
    }

    public Collection<Quest> getAllQuests() {
        return quests.values();
    }

    public boolean canUnlockQuest(Player player, Quest quest) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();
        if (playerQuests.hasActiveQuest(quest.getId()) || playerQuests.hasCompletedQuest(quest.getId())) {
            return false;
        }

        if (data.getExperience().getLevel() < quest.getRequiredLevel()) {
            return false;
        }
        for (String requiredQuestId : quest.getRequiredQuests()) {
            if (!playerQuests.hasCompletedQuest(requiredQuestId)) {
                return false;
            }
        }

        return true;
    }

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
     * 
     * @param player The player who made progress
     * @param type   The type of objective being updated
     * @param target The target descriptor (entity ID, material name, etc.)
     * @param amount The amount to increase progress by (or current value for level
     *               checks)
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

            // Check if this update matches the current objective type
            if (objective.getType() != type)
                continue;

            // Handle objective types differently
            switch (type) {
                case REACH_LEVEL:
                    // For levels, 'amount' is the player's current level
                    // If player is already at or above required level, complete it
                    if (amount >= objective.getRequiredAmount()) {
                        progress.setCurrentProgress(amount);
                        completeStep(player, quest, progress);
                    } else if (amount > progress.getCurrentProgress()) {
                        progress.setCurrentProgress(amount);
                        updateQuestDisplay(player);
                    }
                    break;

                case REACH_LOCATION:
                    // Progress for location is usually just 1 (reached)
                    progress.addProgress(amount);
                    if (currentStep.isComplete(progress.getCurrentProgress())) {
                        completeStep(player, quest, progress);
                    }
                    break;

                case GAIN_EXPERIENCE:
                    // Add the amount of experience gained
                    progress.addProgress(amount);
                    if (currentStep.isComplete(progress.getCurrentProgress())) {
                        completeStep(player, quest, progress);
                    } else {
                        updateQuestDisplay(player);
                    }
                    break;

                default:
                    // Target-based objectives (kill, break, place, etc.)
                    if (objective.getTarget().equalsIgnoreCase(target)) {
                        progress.addProgress(amount);
                        if (currentStep.isComplete(progress.getCurrentProgress())) {
                            completeStep(player, quest, progress);
                        } else {
                            updateQuestDisplay(player);
                        }
                    }
                    break;
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
            plugin.getActionBarManager().setFallbackMessage(player, null);
            return;
        }

        Quest quest = getQuest(trackedProgress.getQuestId());
        if (quest == null) {
            plugin.getActionBarManager().setFallbackMessage(player, null);
            return;
        }

        QuestStep currentStep = quest.getStep(trackedProgress.getCurrentStep());
        if (currentStep == null) {
            plugin.getActionBarManager().setFallbackMessage(player, null);
            return;
        }

        String arrow = getNavigationArrow(player, currentStep.getObjective().getLocation());
        String progressStr = currentStep.getProgressString(trackedProgress.getCurrentProgress());
        if (!progressStr.isEmpty())
            progressStr = " §7(" + progressStr + ")";

        String message = "§6" + quest.getName() + " §8[§e" + (trackedProgress.getCurrentStep() + 1) +
                "§7/§e" + quest.getTotalSteps() + "§8] " + (arrow.isEmpty() ? "" : arrow + " ") + "§f"
                + currentStep.getDescription() + progressStr;

        plugin.getActionBarManager().setFallbackMessage(player, message);
    }


    /**
     * Get an arrow pointing towards a location
     */
    private String getNavigationArrow(Player player, Location target) {
        if (target == null || !player.getWorld().equals(target.getWorld())) {
            return "";
        }

        Vector playerDir = player.getLocation().getDirection().setY(0).normalize();
        Vector targetDir = target.toVector().subtract(player.getLocation().toVector()).setY(0).normalize();

        double angle = Math.toDegrees(
                Math.atan2(targetDir.getZ(), targetDir.getX()) - Math.atan2(playerDir.getZ(), playerDir.getX()));

        if (angle < 0)
            angle += 360;

        if (angle > 337.5 || angle <= 22.5)
            return "§e⬆";
        if (angle > 22.5 && angle <= 67.5)
            return "§e⬈";
        if (angle > 67.5 && angle <= 112.5)
            return "§e➡";
        if (angle > 112.5 && angle <= 157.5)
            return "§e⬊";
        if (angle > 157.5 && angle <= 202.5)
            return "§e⬇";
        if (angle > 202.5 && angle <= 247.5)
            return "§e⬋";
        if (angle > 247.5 && angle <= 292.5)
            return "§e⬅";
        if (angle > 292.5 && angle <= 337.5)
            return "§e⬉";

        return "§e⬆";
    }

    /**
     * Handle when a player talks to an NPC
     */
    public void handleNPCTalk(Player player, String npcName) {
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

            if (objective.getType() == QuestObjective.ObjectiveType.TALK_TO_NPC
                    && objective.getTarget().equalsIgnoreCase(npcName)) {
                completeStep(player, quest, progress);
            } else if (objective.getType() == QuestObjective.ObjectiveType.DELIVER_ITEM
                    && objective.getTarget().startsWith(npcName + ":")) {
                // Format: NPC_NAME:MATERIAL
                String[] parts = objective.getTarget().split(":");
                if (parts.length < 2)
                    continue;

                String targetNpc = parts[0];
                Material material = Material.valueOf(parts[1].toUpperCase());

                if (targetNpc.equalsIgnoreCase(npcName)) {
                    int needed = objective.getRequiredAmount() - progress.getCurrentProgress();
                    int has = 0;

                    for (ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType() == material) {
                            has += item.getAmount();
                        }
                    }

                    if (has > 0) {
                        int toRemove = Math.min(has, needed);
                        removeFromInventory(player, material, toRemove);
                        updateQuestProgress(player, QuestObjective.ObjectiveType.DELIVER_ITEM, objective.getTarget(),
                                toRemove);
                        player.sendMessage("§aYou delivered §f" + toRemove + "x " + material.name() + " §ato §f"
                                + npcName + "§a.");
                    } else {
                        player.sendMessage(
                                "§cYou don't have any §f" + material.name() + " §cto give to §f" + npcName + "§c.");
                    }
                }
            }
        }
    }

    private void removeFromInventory(Player player, Material material, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && item.getType() == material) {
                if (item.getAmount() <= remaining) {
                    remaining -= item.getAmount();
                    player.getInventory().setItem(i, null);
                } else {
                    item.setAmount(item.getAmount() - remaining);
                    remaining = 0;
                }
            }
            if (remaining <= 0)
                break;
        }
    }

    /**
     * Start periodic quest display updates and location checks
     */
    public void startQuestDisplayTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                checkLocationObjectives(player);

                // Update action bar
                updateQuestDisplay(player);
            }
        }, 10L, 2L); // Update every 0.1 seconds (2 ticks) for smooth arrow
    }

    private void checkLocationObjectives(Player player) {
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
            if (objective.getType() == QuestObjective.ObjectiveType.REACH_LOCATION && objective.getLocation() != null) {
                if (objective.getLocation().getWorld().equals(player.getWorld()) &&
                        objective.getLocation().distance(player.getLocation()) <= 3.0) {
                    updateQuestProgress(player, QuestObjective.ObjectiveType.REACH_LOCATION, "", 1);
                }
            }
        }
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
