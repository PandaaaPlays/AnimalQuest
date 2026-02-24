package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.Aptitudes;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AptitudesGUI extends AnimalQuestGUI {
    public AptitudesGUI() {
        super(27, "&8Aptitudes &8&l» &8Configuration");
    }

    public void openInventory(Player player) {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, getFillerItem());
        }

        inventory.setItem(0, getPlayerAptitudeStatsItem(player));
        inventory.setItem(11, getStrengthItem(player));
        inventory.setItem(13, getHealthItem(player));
        inventory.setItem(15, getManaItem(player));

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

        ItemStack item = event.getView().getTopInventory().getItem(event.getSlot());
        if (item == null || item.getType() == Material.GRAY_STAINED_GLASS_PANE)
            return;
        Player clicker = (Player) event.getWhoClicked();
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(clicker.getUniqueId());
        Aptitudes aptitudes = playerData.getAptitudes();

        int clickedSlot = event.getSlot();
        int levelPoints = playerData.getExperience().getLevel();
        int remainingPoints = levelPoints - aptitudes.getTotalPointsUsed();

        switch (clickedSlot) {
            case 11: // Strength
                if (event.isLeftClick()) {
                    if (remainingPoints > 0) {
                        aptitudes.setStrength(aptitudes.getStrength() + 1);
                        openInventory(clicker);
                    }
                } else if (event.isRightClick()) {
                    if (aptitudes.getStrength() > 0) {
                        aptitudes.setStrength(aptitudes.getStrength() - 1);
                        openInventory(clicker);
                    }
                }
                break;
            case 13: // Health
                if (event.isLeftClick()) {
                    if (remainingPoints > 0) {
                        aptitudes.setHealth(aptitudes.getHealth() + 1);
                        playerData.applyHealthAptitude();
                        openInventory(clicker);
                    }
                } else if (event.isRightClick()) {
                    if (aptitudes.getHealth() > 0) {
                        aptitudes.setHealth(aptitudes.getHealth() - 1);
                        playerData.applyHealthAptitude();
                        openInventory(clicker);
                    }
                }
                break;
            case 15: // Mana
                if (event.isLeftClick()) {
                    if (remainingPoints > 0) {
                        aptitudes.setMana(aptitudes.getMana() + 1);
                        openInventory(clicker);
                    }
                } else if (event.isRightClick()) {
                    if (aptitudes.getMana() > 0) {
                        aptitudes.setMana(aptitudes.getMana() - 1);
                        openInventory(clicker);
                    }
                }
                break;
            default:
                break;
        }
    }

    private ItemStack getPlayerAptitudeStatsItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        int levelPoints = playerData.getExperience().getLevel();
        int remainingPoints = levelPoints - playerData.getAptitudes().getTotalPointsUsed();

        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.setDisplayName(Utils.applyFormat("&3&lAptitudes"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat(
            "&f&l⁎ &bRemaining &3&laptitude &bpoints: &f" + remainingPoints));
        lore.add(" ");
        lore.add(Utils.applyFormat("&4&l[&c&lStrength&4&l] &f"
            + playerData.getAptitudes().getStrength()));
        lore.add(Utils.applyFormat(
            "&2&l[&a&lHealth&2&l] &f" + playerData.getAptitudes().getHealth()));
        lore.add(Utils.applyFormat(
            "&3&l[&b&lMana&3&l] &f" + playerData.getAptitudes().getMana()));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }

    private ItemStack getStrengthItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        int strengthPoints = playerData.getAptitudes().getStrength();
        double aptitudeStrength = 1 + (strengthPoints * 0.04D);
        double nextAptitudeStrength = aptitudeStrength + 0.04D;

        ItemStack item = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.addEnchant(Enchantment.SHARPNESS, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(Utils.applyFormat("&c&lStrength"));

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&f&l⁎ &b&lCurrent&b: &7(&3Rank &b"
            + strengthPoints + "&7)"));
        lore.add(Utils.applyFormat(
            "&a&l" + String.format("%.1f", aptitudeStrength * 100) + "% &7damages"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&f&l⁎ &b&lNext&b: &7(&3Rank &b"
            + (strengthPoints + 1) + "&7)"));
        lore.add(Utils.applyFormat(
            "&6&l" + String.format("%.1f", nextAptitudeStrength * 100) + "% &7damages"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&7(( Left-Click to add one ))"));
        lore.add(Utils.applyFormat("&7(( Right-Click to remove one ))"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }

    private ItemStack getHealthItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        int healthPoints = playerData.getAptitudes().getHealth();
        double healthFromLevel = playerData.getExperience().getLevel() / 5D * 2D;
        double healthFromAptitude = (double) (healthPoints * 2) / 3;
        double nextHealthFromAptitude = (double) ((healthPoints + 1) * 2) / 3;

        ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.addEnchant(Enchantment.PROTECTION, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Utils.applyFormat("&a&lHealth"));

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&f&l⁎ &b&lCurrent&b: &7(&3Rank &b"
            + healthPoints + "&7)"));
        lore.add(Utils.applyFormat(
            "&a&l" + String.format("%.1f", (20D + healthFromLevel + healthFromAptitude) / 2) + " &7hearts"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&f&l⁎ &b&lNext&b: &7(&3Rank &b"
            + (healthPoints + 1) + "&7)"));
        lore.add(Utils.applyFormat("&6&l"
            + String.format("%.1f", (20D + healthFromLevel + nextHealthFromAptitude) / 2) + " &7hearts"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&7(( Left-Click to add one ))"));
        lore.add(Utils.applyFormat("&7(( Right-Click to remove one ))"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }

    private ItemStack getManaItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        int manaPoints = playerData.getAptitudes().getMana();
        int maxMana = manaPoints * 8;
        int nextMaxMana = maxMana + 8;

        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Utils.applyFormat("&b&lMana"));

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat(
            "&f&l⁎ &b&lCurrent&b: &7(&3Rank &b" + manaPoints + "&7)"));
        lore.add(Utils.applyFormat("&a&l" + maxMana + " &7mana"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&f&l⁎ &b&lNext&b: &7(&3Rank &b"
            + (manaPoints + 1) + "&7)"));
        lore.add(Utils.applyFormat("&6&l" + nextMaxMana + " &7mana"));
        lore.add(" ");
        lore.add(Utils.applyFormat("&7(( Left-Click to add one ))"));
        lore.add(Utils.applyFormat("&7(( Right-Click to remove one ))"));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return getMenuItem(item, true);
    }
}