package ca.pandaaa.animalquest.guis.quests;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.AnimalQuestGUI;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Quests;
import ca.pandaaa.animalquest.quests.Quest;
import ca.pandaaa.animalquest.quests.QuestProgress;
import ca.pandaaa.animalquest.quests.QuestStep;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class QuestDetailsGUI extends AnimalQuestGUI {
    public static final String TITLE_PREFIX = "&8Quests &8&l>> &8Details";

    public QuestDetailsGUI() {
        super(54, TITLE_PREFIX);
    }

    public void openInventory(Player player, String questId, String fromMenu) {
        Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);
        if (quest == null)
            return;

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        Quests playerQuests = data.getQuests();
        QuestProgress progress = playerQuests.getQuestProgress(questId);

        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, getFillerItem());
        }

        // Quest icon and info
        ItemStack questIcon = quest.getIcon().clone();
        ItemMeta meta = questIcon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat("&6&l" + quest.getName()));
            List<String> lore = new ArrayList<>();
            lore.add("&7" + quest.getDescription());
            lore.add("");
            lore.add("&e&lRequirements:");
            lore.add("&e • &fLevel " + quest.getRequiredLevel());

            if (!quest.getRequiredQuests().isEmpty()) {
                for (String reqId : quest.getRequiredQuests()) {
                    Quest reqQuest = AnimalQuest.getPlugin().getQuestManager().getQuest(reqId);
                    if (reqQuest != null) {
                        boolean completed = playerQuests.hasCompletedQuest(reqId);
                        lore.add((completed ? "&a • &f" : "&c • &f") + reqQuest.getName()
                                + (completed ? " &a\u2713" : " &c\u2717"));
                    }
                }
            }

            if (AnimalQuest.getPlugin().getQuestManager().canUnlockQuest(player, quest)) {
                lore.addAll(List.of("", "&7&o(( Click to start ))"));
            }

            meta.setLore(Utils.applyFormat(lore));
            questIcon.setItemMeta(meta);
        }
        inventory.setItem(3, getMenuItem(questIcon, true));

        // Rewards section
        List<String> rewardLore = new ArrayList<>();
        rewardLore.add("");
        rewardLore.addAll(quest.getReward().getRewardDescription());
        ItemStack rewardsItem = createItem(Material.TOTEM_OF_UNDYING, "&6&lQuest Rewards", rewardLore);
        inventory.setItem(5, rewardsItem);

        // Quest steps
        List<QuestStep> steps = quest.getSteps();
        int currentStepIndex = progress != null ? progress.getCurrentStep() : -1;

        for (int i = 0; i < steps.size() && i < 7; i++) {
            QuestStep step = steps.get(i);
            Material stepMaterial;
            String stepStatus;
            List<String> stepLore = new ArrayList<>();

            if (progress != null && i < currentStepIndex) {
                stepMaterial = Material.LIME_DYE;
                stepStatus = "&a&l";
                stepLore.add("&7Status: &a&lCompleted!");
            } else if (progress != null && i == currentStepIndex) {
                stepMaterial = Material.YELLOW_DYE;
                stepStatus = "&e&l";
                stepLore.add("&7Status: &e&lIn Progress");
                stepLore.add("&7Progress: &f" + step.getProgressString(progress.getCurrentProgress()));
            } else {
                stepMaterial = Material.GRAY_DYE;
                stepStatus = "&7&l";
                stepLore.add("&7Status: &8Not started");
            }

            stepLore.add("");
            stepLore.add("&7Description:");
            stepLore.add("&f" + step.getDescription());

            ItemStack stepItem = createItem(stepMaterial, stepStatus + "Step " + (i + 1), stepLore);
            inventory.setItem(19 + i, stepItem);
        }

        // Action buttons
        /*
         * if (progress != null) {
         * // Track/Untrack button
         * boolean isTracked = questId.equals(playerQuests.getTrackedQuestId());
         * ItemStack trackButton = createItem(
         * isTracked ? Material.ENDER_EYE : Material.ENDER_PEARL,
         * isTracked ? "&a&lTracked" : "&6&lTrack Quest",
         * List.of(isTracked ? "&7Currently displayed above hotbar" :
         * "&7Display progress above hotbar",
         * "", isTracked ? "&7&o(( Click to untrack ))" : "&7&o(( Click to track ))"));
         * inventory.setItem(53, trackButton);
         * 
         * // Abandon button
         * ItemStack abandonButton = createItem(Material.BARRIER, "&c&lAbandon Quest",
         * List.of("&7Click to abandon this quest",
         * "&c&lWarning: &7Progress will be lost!", "",
         * "&c&l&o(( Click to abandon ))"));
         * inventory.setItem(53, abandonButton);
         * } else {
         * // Locked
         * ItemStack lockedButton = createItem(Material.BARRIER, "&c&lLocked",
         * List.of("&7You don't meet the requirements", "&7to start this quest.", "",
         * "&c&o(( Locked ))"));
         * inventory.setItem(53, lockedButton);
         * }
         */
        // Back button
        inventory.setItem(45, getPreviousItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getClickedInventory()))
            return;
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR || !clicked.hasItemMeta())
            return;

        ItemStack iconItem = inventory.getItem(3);
        if (iconItem == null || !iconItem.hasItemMeta())
            return;
        String questName = iconItem.getItemMeta().getDisplayName();

        Quest quest = null;
        for (Quest q : AnimalQuest.getPlugin().getQuestManager().getAllQuests()) {
            if (Utils.applyFormat("&6&l" + q.getName()).equals(questName)) {
                quest = q;
                break;
            }
        }

        if (quest == null)
            return;

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        String displayName = clicked.getItemMeta().getDisplayName();

        if (event.getSlot() == 45) {
            String from = "Main Menu";
            if (clicked.getItemMeta().getLore() != null) {
                for (String line : clicked.getItemMeta().getLore()) {
                    if (line.contains("Active"))
                        from = "Active";
                    else if (line.contains("Available"))
                        from = "Available";
                    else if (line.contains("Completed"))
                        from = "Completed";
                }
            }

            if (from.equals("Active"))
                new ActiveQuestsGUI().openInventory(player);
            else if (from.equals("Available"))
                new AvailableQuestsGUI().openInventory(player);
            else if (from.equals("Completed"))
                new CompletedQuestsGUI().openInventory(player);
            else
                new QuestsGUI().openInventory(player);
        } else if (event.getSlot() == 3 || event.getSlot() == 5) {
            if (!data.getQuests().hasActiveQuest(quest.getId()) && !data.getQuests().hasCompletedQuest(quest.getId())) {
                if (AnimalQuest.getPlugin().getQuestManager().startQuest(player, quest.getId())) {
                    new ActiveQuestsGUI().openInventory(player);
                }
            }
        } else if (displayName.contains("Track")) {
            if (displayName.contains("Tracked")) {
                data.getQuests().setTrackedQuestId(null);
                player.sendMessage(Utils.applyFormat("&7Quest untracked"));
            } else {
                data.getQuests().setTrackedQuestId(quest.getId());
                player.sendMessage(Utils.applyFormat("&aQuest tracked!"));
            }
            openInventory(player, quest.getId(), "Active");
        } else if (displayName.contains("Abandon")) {
            AnimalQuest.getPlugin().getQuestManager().abandonQuest(player, quest.getId());
            new QuestsGUI().openInventory(player);
        }
    }
}
