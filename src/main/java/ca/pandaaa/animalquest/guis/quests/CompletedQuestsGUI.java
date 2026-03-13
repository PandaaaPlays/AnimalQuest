package ca.pandaaa.animalquest.guis.quests;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.AnimalQuestGUI;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.quests.Quest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompletedQuestsGUI extends AnimalQuestGUI {
    public static final String TITLE = "&8Quests &8&l>> &8Completed";

    public CompletedQuestsGUI() {
        super(54, TITLE);
    }

    public void openInventory(Player player) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        Set<String> completedIds = data.getQuests().getCompletedQuests();

        inventory.clear();
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, getFillerItem());
        }

        int slot = 0;
        for (String questId : completedIds) {
            Quest quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);
            if (quest != null) {
                inventory.setItem(slot, createCompletedQuestItem(quest));
                slot++;
            }
        }

        inventory.setItem(45, getPreviousItem());
        player.openInventory(inventory);
    }

    private ItemStack createCompletedQuestItem(Quest quest) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat("&2&l\u2713 " + quest.getName()));
            List<String> lore = new ArrayList<>();
            lore.add("&7" + quest.getDescription());
            lore.add("");
            lore.add("&a&lCompleted!");
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
                new QuestDetailsGUI().openInventory(player, quest.getId(), "Completed");
                return;
            }
        }
    }
}
