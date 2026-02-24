package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.managers.JobRewardManager;
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

    private static final String TITLE = Utils.applyFormat("&8Jobs &8&l>> &8Progress");

    public JobsGUI() {
        super(45, TITLE);
    }

    public void openInventory(Player player, Jobs jobs) {
        Inventory inv = Bukkit.createInventory(null, 45, TITLE);

        // Fill background
        ItemStack filler = createFillerItem();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }

        // Job items
        inv.setItem(20, createJobItem(Job.LUMBERJACK, jobs.getLumberjack()));
        inv.setItem(21, createJobItem(Job.MINER, jobs.getMiner()));
        inv.setItem(23, createJobItem(Job.ALCHEMIST, jobs.getAlchemist()));
        inv.setItem(24, createJobItem(Job.EXPLORER, jobs.getExplorer()));

        // Back button (assuming there is one in AnimalQuestGUI or Menu)
        inv.setItem(40, createBackItem());

        player.openInventory(inv);
    }

    private ItemStack createJobItem(Job job, JobProgress progress) {
        ItemStack item = new ItemStack(job.getIcon());
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        meta.setDisplayName(Utils.applyFormat(job.getDisplayName()));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&7" + job.getDescription()));
        lore.add("");
        lore.add(Utils.applyFormat("&bLevel: &f" + progress.getLevel() + " &8/ &f" + progress.getMaxLevel()));
        lore.add(createExpBar(progress.getExperience(), progress.getGoalExperience(), 20));
        lore.add("");

        lore.add(Utils.applyFormat("&3&lUnlocks:"));
        // Current level unlocks
        for (int i = 1; i <= progress.getLevel(); i++) {
            List<String> r = JobRewardManager.getRewardsForLevel(job, i);
            if (!r.isEmpty()) {
                for (String s : r)
                    lore.add(Utils.applyFormat(" &a✔ " + s));
            }
        }

        // Next level unlocks
        if (progress.getLevel() < progress.getMaxLevel()) {
            for (int i = progress.getLevel() + 1; i <= progress.getMaxLevel(); i++) {
                List<String> r = JobRewardManager.getRewardsForLevel(job, i);
                if (!r.isEmpty()) {
                    lore.add(Utils.applyFormat("&8&lNext at Lvl " + i + ":"));
                    for (String s : r)
                        lore.add(Utils.applyFormat(" &c✖ " + s));
                    break;
                }
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createFillerItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createBackItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat("&c&lBack"));
            item.setItemMeta(meta);
        }
        return item;
    }

    private String createExpBar(double current, double goal, int barLength) {
        if (goal <= 0) {
            return Utils.applyFormat("&a" + "■".repeat(barLength) + " &7MAX");
        }
        int filled = (int) Math.min(barLength, (current / goal) * barLength);
        String green = "&b" + "■".repeat(filled);
        String gray = "&8" + "■".repeat(barLength - filled);
        return Utils.applyFormat("&r" + green + gray + " &f" + (int) current + "&7/&f" + (int) goal);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory())) {
            return;
        }

        event.setCancelled(true);
        if (event.getSlot() == 40) {
            Player player = (Player) event.getWhoClicked();
            new MenuGUI().openInventory(player);
        }
    }
}