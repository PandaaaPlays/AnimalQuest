package ca.pandaaa.animalquest.guis.quests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import ca.pandaaa.animalquest.guis.AnimalQuestGUI;

import java.util.List;

public class QuestsGUI extends AnimalQuestGUI {
    private static final String MAIN_MENU_TITLE = "&8Quests &8&l>> &8Main Menu";

    public QuestsGUI() {
        super(27, MAIN_MENU_TITLE);
    }

    public void openInventory(Player player) {
        inventory.clear();
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, getFillerItem());
        }

        inventory.setItem(11, getActiveQuestsCategoryItem());
        inventory.setItem(13, getAvailableQuestsCategoryItem());
        inventory.setItem(15, getCompletedQuestsCategoryItem());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getClickedInventory()))
            return;
        if (!(event.getWhoClicked() instanceof Player player))
            return;

        event.setCancelled(true);

        if (event.getSlot() == 11) {
            new ActiveQuestsGUI().openInventory(player);
        } else if (event.getSlot() == 13) {
            new AvailableQuestsGUI().openInventory(player);
        } else if (event.getSlot() == 15) {
            new CompletedQuestsGUI().openInventory(player);
        }
    }

    private ItemStack getActiveQuestsCategoryItem() {
        return createItem(Material.WRITABLE_BOOK, "&a&lActive Quests", List.of("&7Click to view your current quests"));
    }

    private ItemStack getAvailableQuestsCategoryItem() {
        return createItem(Material.BOOK, "&b&lAvailable Quests", List.of("&7Click to browse new quests"));
    }

    private ItemStack getCompletedQuestsCategoryItem() {
        return createItem(Material.ENCHANTED_BOOK, "&2&lCompleted Quests", List.of("&7Click to view finished quests"));
    }
}
