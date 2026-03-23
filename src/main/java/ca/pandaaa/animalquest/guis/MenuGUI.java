package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.quests.QuestsGUI;
import ca.pandaaa.animalquest.player.Aptitudes;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Formats;
import ca.pandaaa.animalquest.utils.GlobalMultiplier;
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

public class MenuGUI extends AnimalQuestGUI {

    private static final String TITLE = Utils.applyFormat("&8AnimalQuest &8&l>> &8Menu");
    private static final int SLOT_STATISTICS = 11;
    private static final int SLOT_QUESTS = 12;
    private static final int SLOT_JOBS = 13;
    private static final int SLOT_APTITUDES = 14;
    private static final int SLOT_MOUNTS = 16;
    private static final int SLOT_GUILDS = 15;
    private static final int SLOT_HOME = 10;

    public MenuGUI() {
        super(27, TITLE);
    }

    public void openInventory(Player player) {
        inventory.clear();
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, getFillerItem());
        }

        inventory.setItem(SLOT_STATISTICS, menuPlayerStatisticsItem(player));
        inventory.setItem(SLOT_QUESTS, menuQuestsItem());
        inventory.setItem(SLOT_JOBS, menuJobsItem());
        inventory.setItem(SLOT_APTITUDES, menuAptitudesItem());
        inventory.setItem(SLOT_MOUNTS, menuMountsItem());
        inventory.setItem(SLOT_GUILDS, menuGuildsItem());
        inventory.setItem(SLOT_HOME, menuTeleportationItem(player));

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
        return getMenuItem(item, true);
    }

    private ItemStack menuQuestsItem() {
        return createItem(Material.BOOK, "&6&lQuests",
                List.of("&7View your active and available quests.", "", "&eClick to open!"));
    }

    private ItemStack menuJobsItem() {
        return createItem(Material.GOLDEN_PICKAXE, "&b&lJobs",
                List.of("&7Check your job progress and perks.", "", "&eClick to open!"));
    }

    private ItemStack menuAptitudesItem() {
        return createItem(Material.EXPERIENCE_BOTTLE, "&a&lAptitudes",
                List.of("&7Improve your strength, health and mana.", "", "&eClick to open!"));
    }

    private ItemStack menuMountsItem() {
        return createItem(Material.SADDLE, "&d&lMounts",
                List.of("&7Purchase and manage your mounts.", "", "&eClick to open!"));
    }

    private ItemStack menuGuildsItem() {
        return createItem(Material.SHIELD, "&2&lGuilds",
                List.of("&7Manage your guild and members.", "", "&cComing soon!"));
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

        if (slot == SLOT_QUESTS) {
            new QuestsGUI().openInventory(player);
        } else if (slot == SLOT_JOBS) {
            PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
            if (data != null)
                new JobsGUI().openInventory(player, data.getJobs());
        } else if (slot == SLOT_APTITUDES) {
            new AptitudesGUI().openInventory(player);
        } else if (slot == SLOT_MOUNTS) {
            new MountShopGUI(player).open();
        } else if (slot == SLOT_HOME) {
            AnimalQuest.getPlugin().getHomeManager().teleportToHome(player);
        }
    }
}