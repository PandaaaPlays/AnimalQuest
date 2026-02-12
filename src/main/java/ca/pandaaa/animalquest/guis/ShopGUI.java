package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.shop.Shop;
import ca.pandaaa.animalquest.shop.ShopItem;
import ca.pandaaa.animalquest.utils.Formats;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopGUI extends AnimalQuestGUI {

    private final Shop shop;
    private final Map<Integer, ShopItem> slotToItemMap;

    public ShopGUI(Shop shop) {
        super(calculateSize(shop.getItems().size()), "&8Shop &8&l>> &8" + shop.getName());
        this.shop = shop;
        this.slotToItemMap = new HashMap<>();
        initializeItems();
    }

    private static int calculateSize(int itemCount) {
        if (itemCount <= 0)
            return 9;
        int rows = (int) Math.ceil(itemCount / 9.0);
        return Math.min(6, rows) * 9;
    }

    private void initializeItems() {
        int slot = 0;
        for (ShopItem shopItem : shop.getItems()) {
            if (slot >= inventory.getSize())
                break;

            ItemStack displayItem = shopItem.getItem().getItemStack(shopItem.getAmount());
            ItemMeta meta = displayItem.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add(" ");
                lore.add(Utils.applyFormat("&5&l⁎ &d[Buy Price]"));
                if (shopItem.getPriceMoney() > 0) {
                    lore.add(Utils.applyFormat(" &5&l- &f$" + Formats.formatMoney(shopItem.getPriceMoney())));
                }

                for (ItemStack item : shopItem.getPriceItems()) {
                    String itemName;
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        itemName = item.getItemMeta().getDisplayName();
                    } else if (item.getType() == Material.LAPIS_LAZULI) {
                        itemName = Utils.applyFormat(item.getAmount() != 1 ? "&9Sapphires" : "&9Sapphire");
                    } else {
                        itemName = Utils.getSentenceCase(item.getType().toString());
                    }

                    if (item.getType() == Material.LAPIS_LAZULI) {
                        lore.add(Utils.applyFormat(" &5&l- &9&lx" + item.getAmount() + " " + itemName.substring(2)));
                    } else {
                        lore.add(Utils.applyFormat(" &5&l- &fx" + item.getAmount() + " " + itemName));
                    }
                }

                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }

            inventory.setItem(slot, getMenuItem(displayItem, false));
            slotToItemMap.put(slot, shopItem);
            slot++;
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory())) {
            return;
        }

        if (event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if (!slotToItemMap.containsKey(slot))
            return;

        ShopItem shopItem = slotToItemMap.get(slot);
        purchaseItem(player, shopItem);
    }

    private void purchaseItem(Player player, ShopItem shopItem) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        if (data == null)
            return;

        if (data.getBalance() < shopItem.getPriceMoney()) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou don't have enough money to purchase this item."));
            return;
        }

        for (ItemStack item : shopItem.getPriceItems()) {
            if (!hasRequiredItem(player, item)) {
                player.sendMessage(
                        Utils.applyFormat("&c&l[!] &cYou don't have the required items to purchase this item."));
                return;
            }
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYour inventory is full."));
            return;
        }

        if (shopItem.getPriceMoney() > 0) {
            data.setBalance((int) (data.getBalance() - shopItem.getPriceMoney()));
        }

        for (ItemStack item : shopItem.getPriceItems()) {
            removeRequiredItem(player, item);
        }

        player.getInventory().addItem(shopItem.getItem().getItemStack(shopItem.getAmount()));
        if (shopItem.shouldBroadcast()) {
            Bukkit.broadcastMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &8&l>> &3" + player.getName()
                    + " &bpurchased " + shopItem.getItem().getName() + "."));
        }
    }

    private boolean hasRequiredItem(Player player, ItemStack required) {
        int amountFound = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR)
                continue;
            if (isSimilarIgnoreDamage(item, required)) {
                amountFound += item.getAmount();
            }
        }
        return amountFound >= required.getAmount();
    }

    private void removeRequiredItem(Player player, ItemStack required) {
        int toRemove = required.getAmount();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR)
                continue;
            if (isSimilarIgnoreDamage(item, required)) {
                if (item.getAmount() > toRemove) {
                    item.setAmount(item.getAmount() - toRemove);
                    toRemove = 0;
                } else {
                    toRemove -= item.getAmount();
                    player.getInventory().setItem(i, null);
                }
            }
            if (toRemove <= 0)
                break;
        }
    }

    private boolean isSimilarIgnoreDamage(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null)
            return stack1 == stack2;
        if (stack1.getType() != stack2.getType())
            return false;
        if (stack1.isSimilar(stack2))
            return true;

        ItemMeta meta1 = stack1.getItemMeta();
        ItemMeta meta2 = stack2.getItemMeta();
        if (meta1 instanceof Damageable d1 && meta2 instanceof Damageable d2) {
            d1.setDamage(0);
            d2.setDamage(0);

            ItemStack clone1 = stack1.clone();
            clone1.setItemMeta(meta1);
            ItemStack clone2 = stack2.clone();
            clone2.setItemMeta(meta2);

            return clone1.isSimilar(clone2);
        }
        return false;
    }
}
