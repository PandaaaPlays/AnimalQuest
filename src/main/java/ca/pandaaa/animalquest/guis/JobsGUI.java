package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.player.jobs.JobProgress;
import ca.pandaaa.animalquest.player.jobs.Jobs;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class JobsGUI extends AnimalQuestGUI implements EventListener {

    private static final String TITLE = Utils.applyFormat("&8» &eJobs");
    private static final int SLOT_LUMBERJACK = 10;
    private static final int SLOT_MINER = 12;
    private static final int SLOT_ALCHEMIST = 14;
    private static final int SLOT_EXPLORER = 16;

    public JobsGUI() {
        super(27, TITLE);
    }

    public void openInventory(Player player, Jobs jobs) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        inv.setItem(SLOT_LUMBERJACK, createJobItem(Material.OAK_LOG, "&6Lumberjack", jobs.getLumberjack()));
        inv.setItem(SLOT_MINER, createJobItem(Material.DIAMOND_PICKAXE, "&7Miner", jobs.getMiner()));
        inv.setItem(SLOT_ALCHEMIST, createJobItem(Material.BREWING_STAND, "&dAlchemist", jobs.getAlchemist()));
        inv.setItem(SLOT_EXPLORER, createJobItem(Material.COMPASS, "&bExplorer", jobs.getExplorer()));

        player.openInventory(inv);
    }

    private ItemStack createJobItem(Material material, String displayName, JobProgress progress) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        meta.setDisplayName(Utils.applyFormat(displayName));
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ARMOR_TRIM,
            ItemFlag.HIDE_DYE);

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&7Lvl &f" + progress.getLevel() + "&7/&f" + progress.getMaxLevel()));
        lore.add(createExpBar(progress.getExperience(), progress.getGoalExperience(), 20));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String createExpBar(double current, double goal, int barLength) {
        int currentInt = (int) current;
        if (goal <= 0) {
            return Utils.applyFormat(currentInt + " &a" + "|".repeat(barLength) + " &7MAX");
        }
        int goalInt = (int) goal;
        int filled = (int) Math.min(barLength, (current / goal) * barLength);
        String green = "&a" + "|".repeat(filled);
        String gray = "&7" + "|".repeat(barLength - filled);
        return Utils.applyFormat(currentInt + " " + green + gray + " " + goalInt);
    }

    public boolean isJobsGUI(String title) {
        return title != null && title.equals(TITLE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isJobsGUI(event.getView().getTitle())) {
            return;
        }
        event.setCancelled(true);
    }
}