package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JobRewardManager {

    public static List<String> getRewardsForLevel(Job job, int level) {
        List<String> rewards = new ArrayList<>();
        switch (job) {
            case LUMBERJACK:
                if (level == 1)
                    rewards.add("&fUnlock: &7Oak & Birch");
                if (level == 5)
                    rewards.add("&fUnlock: &7Spruce & Jungle");
                if (level == 10)
                    rewards.add("&fUnlock: &7Acacia & Dark Oak");
                if (level == 15)
                    rewards.add("&fUnlock: &7Mangrove & Cherry");
                if (level == 20)
                    rewards.add("&fUnlock: &7Crimson & Warped");
                rewards.add("&bRare Drop: &fDead Bush (Lvl " + getRareDropLevelRange(level) + ")");
                break;
            case MINER:
                if (level == 1)
                    rewards.add("&fUnlock: &7Stone & Coal");
                if (level == 5)
                    rewards.add("&fUnlock: &7Iron & Copper");
                if (level == 10)
                    rewards.add("&fUnlock: &7Gold & Lapis");
                if (level == 15)
                    rewards.add("&fUnlock: &7Redstone & Diamond");
                if (level == 20)
                    rewards.add("&fUnlock: &7Emerald & Netherite");
                rewards.add("&bRare Drop: &fRare Rock (Lvl " + getRareDropLevelRange(level) + ")");
                break;
            case ALCHEMIST:
                rewards.add("&fBonus: &7Brewing Speed +" + (level * 5) + "%");
                break;
            case EXPLORER:
                rewards.add("&fBonus: &7Speed Boost while running");
                break;
        }
        return rewards;
    }

    private static String getRareDropLevelRange(int jobLevel) {
        int min = 1;
        int max = Math.min(10, (jobLevel / 2) + 1);
        return min + "-" + max;
    }

    public static ItemStack getRareItem(Job job, int itemLevel) {
        Material mat = job == Job.MINER ? Material.FLINT : Material.DEAD_BUSH;
        String name = job == Job.MINER ? "&7Rare Rock" : "&6Ancient Twig";
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat(name + " &8(Lvl " + itemLevel + ")"));
            List<String> lore = new ArrayList<>();
            lore.add(Utils.applyFormat("&7A rare finding from your " + job.name().toLowerCase() + " work."));
            lore.add(Utils.applyFormat("&7Can be sold or used for crafting."));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
