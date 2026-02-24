package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Quests;
import ca.pandaaa.animalquest.quests.Quest;
import ca.pandaaa.animalquest.quests.QuestProgress;
import ca.pandaaa.animalquest.quests.QuestStep;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for managing quests
 */
public class QuestsGUI implements Listener {
    private static final String MAIN_MENU_TITLE = "§6§lQuests";
    private static final String QUEST_DETAILS_TITLE = "§6§lQuest: ";

    public QuestsGUI() {
        Bukkit.getPluginManager().registerEvents(this, AnimalQuest.getPlugin());
    }

    /**
     * Open the main quests menu
     */
    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, MAIN_MENU_TITLE);
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();

        // Active Quests Section (Slots 10-16)
        ItemStack activeHeader = createItem(Material.WRITABLE_BOOK, "§a§lActive Quests",
                List.of("§7Click on a quest to view details"));
        inv.setItem(1, activeHeader);

        int activeSlot = 10;
        for (String questId : playerQuests.getActiveQuests().keySet()) {
            Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);
            if (quest != null && activeSlot <= 16) {
                QuestProgress progress = playerQuests.getQuestProgress(questId);
                inv.setItem(activeSlot, createActiveQuestItem(quest, progress));
                activeSlot++;
            }
        }

        // Available Quests Section (Slots 28-34)
        ItemStack availableHeader = createItem(Material.BOOK, "§e§lAvailable Quests",
                List.of("§7Quests you can start"));
        inv.setItem(19, availableHeader);

        int availableSlot = 28;
        for (Quest quest : AnimalQuest.getPlugin().getQuestManager().getAllQuests()) {
            if (AnimalQuest.getPlugin().getQuestManager().canUnlockQuest(player, quest) && availableSlot <= 34) {
                inv.setItem(availableSlot, createAvailableQuestItem(quest));
                availableSlot++;
            }
        }

        // Completed Quests Section (Slots 46-52)
        ItemStack completedHeader = createItem(Material.ENCHANTED_BOOK, "§2§lCompleted Quests",
                List.of("§7Quests you've finished"));
        inv.setItem(37, completedHeader);

        int completedSlot = 46;
        for (String questId : playerQuests.getCompletedQuests()) {
            Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);
            if (quest != null && completedSlot <= 52) {
                inv.setItem(completedSlot, createCompletedQuestItem(quest));
                completedSlot++;
            }
        }

        // Decorative borders
        ItemStack border = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", List.of());
        for (int i : new int[] { 0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 53 }) {
            inv.setItem(i, border);
        }

        player.openInventory(inv);
    }

    /**
     * Open quest details menu
     */
    public void openQuestDetails(Player player, String questId) {
        Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);
        if (quest == null)
            return;

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();
        QuestProgress progress = playerQuests.getQuestProgress(questId);

        Inventory inv = Bukkit.createInventory(null, 54, QUEST_DETAILS_TITLE + quest.getName());

        // Quest icon and info
        ItemStack questIcon = quest.getIcon();
        ItemMeta meta = questIcon.getItemMeta();
        if (meta != null) {
            List<String> lore = new ArrayList<>();
            lore.add("§7" + quest.getDescription());
            lore.add("");
            lore.add("§eRequired Level: §f" + quest.getRequiredLevel());

            if (!quest.getRequiredQuests().isEmpty()) {
                lore.add("§eRequired Quests:");
                for (String reqId : quest.getRequiredQuests()) {
                    Quest reqQuest = AnimalQuest.getPlugin().getQuestManager().getQuest(reqId);
                    if (reqQuest != null) {
                        boolean completed = playerQuests.hasCompletedQuest(reqId);
                        lore.add((completed ? "  §a✓ " : "  §c✗ ") + reqQuest.getName());
                    }
                }
            }

            meta.setLore(lore);
            questIcon.setItemMeta(meta);
        }
        inv.setItem(4, questIcon);

        // Quest steps
        List<QuestStep> steps = quest.getSteps();
        int currentStepIndex = progress != null ? progress.getCurrentStep() : -1;

        for (int i = 0; i < steps.size() && i < 7; i++) {
            QuestStep step = steps.get(i);
            Material stepMaterial;
            String stepStatus;
            List<String> stepLore = new ArrayList<>();

            if (progress == null) {
                stepMaterial = Material.PAPER;
                stepStatus = "§7";
            } else if (i < currentStepIndex) {
                stepMaterial = Material.LIME_DYE;
                stepStatus = "§a✓ ";
                stepLore.add("§aCompleted!");
            } else if (i == currentStepIndex) {
                stepMaterial = Material.YELLOW_DYE;
                stepStatus = "§e➤ ";
                stepLore.add("§eIn Progress: " + step.getProgressString(progress.getCurrentProgress()));
            } else {
                stepMaterial = Material.GRAY_DYE;
                stepStatus = "§7";
                stepLore.add("§7Not started");
            }

            stepLore.add(0, "§f" + step.getDescription());
            ItemStack stepItem = createItem(stepMaterial, stepStatus + "Step " + (i + 1), stepLore);
            inv.setItem(19 + i, stepItem);
        }

        // Rewards section
        ItemStack rewardsItem = createItem(Material.CHEST, "§6§lRewards", quest.getReward().getRewardDescription());
        inv.setItem(49, rewardsItem);

        // Action buttons
        if (progress != null) {
            // Track/Untrack button
            boolean isTracked = questId.equals(playerQuests.getTrackedQuestId());
            ItemStack trackButton = createItem(
                    isTracked ? Material.ENDER_EYE : Material.ENDER_PEARL,
                    isTracked ? "§a§lTracked" : "§e§lTrack Quest",
                    List.of(isTracked ? "§7Currently displayed above hotbar" : "§7Click to track this quest"));
            inv.setItem(45, trackButton);

            // Abandon button
            ItemStack abandonButton = createItem(Material.BARRIER, "§c§lAbandon Quest",
                    List.of("§7Click to abandon this quest", "§c§lWarning: Progress will be lost!"));
            inv.setItem(53, abandonButton);
        } else if (AnimalQuest.getPlugin().getQuestManager().canUnlockQuest(player, quest)) {
            // Start quest button
            ItemStack startButton = createItem(Material.LIME_DYE, "§a§lStart Quest",
                    List.of("§7Click to begin this quest"));
            inv.setItem(49, startButton);
        } else {
            // Locked
            ItemStack lockedButton = createItem(Material.BARRIER, "§c§lLocked",
                    List.of("§7You don't meet the requirements"));
            inv.setItem(49, lockedButton);
        }

        // Back button
        ItemStack backButton = createItem(Material.ARROW, "§f§lBack", List.of("§7Return to quest menu"));
        inv.setItem(45, backButton);

        player.openInventory(inv);
    }

    /**
     * Create an item with name and lore
     */
    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Create an active quest item
     */
    private ItemStack createActiveQuestItem(Quest quest, QuestProgress progress) {
        ItemStack item = quest.getIcon();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a§l" + quest.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7" + quest.getDescription());
            lore.add("");

            QuestStep currentStep = quest.getStep(progress.getCurrentStep());
            if (currentStep != null) {
                lore.add("§eCurrent Step:");
                lore.add("§f  " + currentStep.getDescription());
                lore.add("§7  Progress: " + currentStep.getProgressString(progress.getCurrentProgress()));
            }

            lore.add("");
            lore.add("§eClick to view details");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Create an available quest item
     */
    private ItemStack createAvailableQuestItem(Quest quest) {
        ItemStack item = quest.getIcon();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e§l" + quest.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7" + quest.getDescription());
            lore.add("");
            lore.add("§eSteps: §f" + quest.getTotalSteps());
            lore.add("");
            lore.addAll(quest.getReward().getRewardDescription());
            lore.add("");
            lore.add("§aClick to start!");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Create a completed quest item
     */
    private ItemStack createCompletedQuestItem(Quest quest) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§2§l✓ " + quest.getName());
            List<String> lore = new ArrayList<>();
            lore.add("§7" + quest.getDescription());
            lore.add("");
            lore.add("§a§lCompleted!");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        String title = event.getView().getTitle();

        if (!title.startsWith("§6§lQuest")) {
            return;
        }

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType() == Material.AIR)
            return;
        if (!clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
            return;

        if (title.equals(MAIN_MENU_TITLE)) {
            handleMainMenuClick(player, clicked);
        } else if (title.startsWith(QUEST_DETAILS_TITLE)) {
            handleQuestDetailsClick(player, clicked, title);
        }
    }

    /**
     * Handle clicks in the main menu
     */
    private void handleMainMenuClick(Player player, ItemStack clicked) {
        String displayName = clicked.getItemMeta().getDisplayName();

        // Find quest by checking all quests
        for (Quest quest : AnimalQuest.getPlugin().getQuestManager().getAllQuests()) {
            String questName = quest.getName();
            if (displayName.contains(questName)) {
                openQuestDetails(player, quest.getId());
                return;
            }
        }
    }

    /**
     * Handle clicks in quest details menu
     */
    private void handleQuestDetailsClick(Player player, ItemStack clicked, String title) {
        String displayName = clicked.getItemMeta().getDisplayName();
        String questName = title.substring(QUEST_DETAILS_TITLE.length());

        // Find the quest
        Quest quest = null;
        for (Quest q : AnimalQuest.getPlugin().getQuestManager().getAllQuests()) {
            if (q.getName().equals(questName)) {
                quest = q;
                break;
            }
        }

        if (quest == null)
            return;

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        if (displayName.contains("Back")) {
            openMainMenu(player);
        } else if (displayName.contains("Start Quest")) {
            player.closeInventory();
            AnimalQuest.getPlugin().getQuestManager().startQuest(player, quest.getId());
        } else if (displayName.contains("Track")) {
            if (displayName.contains("Tracked")) {
                data.getQuests().setTrackedQuestId(null);
                player.sendMessage("§7Quest untracked");
            } else {
                data.getQuests().setTrackedQuestId(quest.getId());
                player.sendMessage("§aQuest tracked!");
            }
            openQuestDetails(player, quest.getId());
        } else if (displayName.contains("Abandon")) {
            player.closeInventory();
            AnimalQuest.getPlugin().getQuestManager().abandonQuest(player, quest.getId());
        }
    }
}
