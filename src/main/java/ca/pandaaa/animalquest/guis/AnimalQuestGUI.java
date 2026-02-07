package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class AnimalQuestGUI implements Listener {
    protected final Inventory inventory;

    public AnimalQuestGUI(int size, String title) {
        title = Utils.applyFormat(title);

        int titleLength = 27;
        Matcher matcher = ChatColor.STRIP_COLOR_PATTERN.matcher(title);
        while(matcher.find())
            titleLength += 2;

        title = (title.length() > titleLength ? title.substring(0, titleLength) + "." : title);

        this.inventory = Bukkit.createInventory(null, size, title);
        AnimalQuest plugin = AnimalQuest.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected boolean isEventRelevant(Inventory clickedInventory) {
        if (!Objects.equals(clickedInventory, inventory))
            return false;

        return true;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if(!Objects.equals(event.getView().getTopInventory(), inventory)) {
            return;
        }
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize() - 1) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private static boolean DEBUG_MENU_ITEM = false;
    protected static ItemStack getMenuItem(ItemStack item, boolean hideItemFlags) {
        ItemStack menuItem = item.clone();
        ItemMeta itemMeta = menuItem.getItemMeta();
        if(DEBUG_MENU_ITEM) {
            List<String> lore = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
            lore.add(Utils.applyFormat("&c&lMENU_ITEM"));
            itemMeta.setLore(lore);
        }
        NamespacedKey key = new NamespacedKey(AnimalQuest.getPlugin(), "AnimalQuest.MenuItem");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        if(hideItemFlags) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        }
        menuItem.setItemMeta(itemMeta);
        return menuItem;
    }

    protected static ItemStack getFillerItem() {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerItemMeta = filler.getItemMeta();
        if(fillerItemMeta != null)
            fillerItemMeta.setDisplayName(" ");
        filler.setItemMeta(fillerItemMeta);
        return getMenuItem(filler, true);
    }
}