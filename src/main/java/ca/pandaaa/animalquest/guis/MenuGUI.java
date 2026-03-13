package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.Aptitudes;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Formats;
import ca.pandaaa.animalquest.utils.GlobalMultiplier;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MenuGUI extends AnimalQuestGUI implements EventListener {

    private static final String TITLE = Utils.applyFormat("&8AnimalQuest &8&l>> &8Menu");
    private static final int SLOT_PLAYER_STATISTICS_ITEM = 0;
    private static final int SLOT_TELEPORTATION_ITEM = 10;

    public MenuGUI() {
        super(27, TITLE);
    }

    public void openInventory(Player player) {
        inventory.setItem(SLOT_PLAYER_STATISTICS_ITEM, menuPlayerStatisticsItem(player));
        inventory.setItem(SLOT_TELEPORTATION_ITEM, menuTeleportationItem(player));

        player.openInventory(inventory);
    }

    private ItemStack menuPlayerStatisticsItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        int playerLevel = playerData.getExperience().getLevel();
        double playerExperience = playerData.getExperience().getExperience();
        double playerExperienceNeeded = playerData.getExperience().getGoalExperience();
        double strengthPercentage = (1 + playerData.getAptitudes().getStrength() * Aptitudes.STRENGTH_MULTIPLIER) * 100;
        double health = (20 +
                (playerLevel * PlayerData.HEALTH_PER_LEVEL_MULTIPLICATOR) +
                (playerData.getAptitudes().getHealth() * Aptitudes.HEALTH_MULTIPLIER));
        int mana = playerData.getAptitudes().getMana() * Aptitudes.MANA_MULTIPLIER;

        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.applyFormat("&b&lStatistics"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&3&l[&b&lLevel&3&l] &f" + playerLevel));
        lore.add(Utils.applyFormat("&3&l[&b&lExperience&3&l] &f" + Formats.formatExperienceScoreboard(playerExperience)
                + "/" + Formats.formatExperienceScoreboard(playerExperienceNeeded)));
        lore.add(Utils.applyFormat(
                "&3&l[&b&lMultiplicator&3&l] &f" + Formats.formatBonus(GlobalMultiplier.getGlobalMultiplier())));
        lore.add(Utils.applyFormat(
                "&3&l[&b&lBonus&3&l] &f" + Formats.formatBonus(GlobalMultiplier.getPlayerMultiplier(player))));
        lore.add("");
        lore.add(Utils
                .applyFormat("&4&l[&c&lStrength&4&l] &f" + String.format("%.1f", strengthPercentage) + "% damages"));
        lore.add(Utils.applyFormat("&2&l[&a&lHealth&2&l] &f" + String.format("%.1f", health / 2) + " hearts"));
        lore.add(Utils.applyFormat("&9&l[&b&lMana&9&l] &f" + mana + " mana"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack menuTeleportationItem(Player player) {
        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        ItemStack item = new ItemStack(Material.TURTLE_SCUTE);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.applyFormat("&b&lMagic stone"));
        List<String> lore = new ArrayList<>();
        String homeName = playerData.getHomeName();
        if (homeName == null || homeName.isEmpty())
            homeName = "None";
        lore.add(Utils.applyFormat("&3&lCurrent home&3: &f" + Utils.getSentenceCase(homeName)));
        lore.add(Utils.applyFormat("&b&l⁎ &fClick on a home NPC to change your spawnpoint."));
        lore.add("");
        lore.add(Utils.applyFormat("&7&o(( Click to teleport back to your home ))"));
        meta.setLore(lore);
        if (AnimalQuest.getPlugin().getHomeManager().getRemainingCooldown(player) <= 0) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
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

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == SLOT_TELEPORTATION_ITEM) {
            AnimalQuest.getPlugin().getHomeManager().teleportToHome(player);
        }

        event.setCancelled(true);
    }
}