package ca.pandaaa.animalquest.guis.quests;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.AnimalQuestGUI;
import ca.pandaaa.animalquest.player.PlayerData;
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
import java.util.Map;

public class ActiveQuestsGUI extends AnimalQuestGUI {
    public static final String TITLE = "&8Quests &8&l>> &8Active";

    public ActiveQuestsGUI() {
        super(54, TITLE);
    }

    public void openInventory(Player player) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        Map<String, QuestProgress> activeQuests = data.getQuests().getActiveQuests();

        inventory.clear();
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, getFillerItem());
        }

        int slot = 0;
        for (Map.Entry<String, QuestProgress> entry : activeQuests.entrySet()) {
            Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(entry.getKey());
            if (quest != null) {
                inventory.setItem(slot, createActiveQuestItem(quest, entry.getValue()));
                slot++;
            }
        }

        inventory.setItem(45, getPreviousItem());
        player.openInventory(inventory);
    }

    private ItemStack createActiveQuestItem(Quest quest, QuestProgress progress) {
        ItemStack item = quest.getIcon();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat("&a&l" + quest.getName()));
            List<String> lore = new ArrayList<>();
            lore.add("&7" + quest.getDescription());
            lore.add("");

            QuestStep currentStep = quest.getStep(progress.getCurrentStep());
            if (currentStep != null) {
                lore.add("&eCurrent Step:");
                lore.add("&f  " + currentStep.getDescription());
                lore.add("&7  Progress: " + currentStep.getProgressString(progress.getCurrentProgress()));
            }

            lore.add("");
            lore.add("&eClick to view details");

            meta.setLore(Utils.applyFormat(lore));
            item.setItemMeta(meta);
        }
        return getMenuItem(item, true);
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

        if (event.getSlot() == 45) {
            new QuestsGUI().openInventory(player);
            return;
        }

        String displayName = clicked.getItemMeta().getDisplayName();
        for (Quest quest : AnimalQuest.getPlugin().getQuestManager().getAllQuests()) {
            if (displayName.contains(quest.getName())) {
                new QuestDetailsGUI().openInventory(player, quest.getId(), "Active");
                return;
            }
        }
    }
}
