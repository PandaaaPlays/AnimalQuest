package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.enums.AnimalQuestItem;
import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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
                    rewards.add("&fLevel 1: &7Oak Tree");
                if (level == 3)
                    rewards.add("&fLevel 3: &7Enchanted Oak Tree");
                if (level == 5)
                    rewards.add("&fLevel 5: &7Dark Oak Tree");
                if (level == 8)
                    rewards.add("&fLevel 8: &7Cactus, Cactus flowers");
                if (level == 10)
                    rewards.add("&fLevel 10: &7Spruce Tree");
                if (level == 15)
                    rewards.add("&fLevel 15: &7Birch Tree");
                if (level == 18)
                    rewards.add("&fLevel 18: &7Burnt Oak Tree (Pale Oak)");
                break;
            case MINER:
                if (level == 1)
                    rewards.add("&fLevel 1: &7Cobblestone");
                if (level == 2)
                    rewards.add("&fLevel 2: &7Coal Ore");
                if (level == 3)
                    rewards.add("&fLevel 3: &7Titanium (Diorite)");
                if (level == 5)
                    rewards.add("&fLevel 5: &7Gold Ore");
                if (level == 8)
                    rewards.add("&fLevel 8: &7Diamond Ore");
                if (level == 10)
                    rewards.add("&fLevel 10: &7Meteorite (Ancient Debris)");
                if (level == 15)
                    rewards.add("&fLevel 15: &7Dragonium (Crying Obsidian)");
                if (level == 18)
                    rewards.add("&fLevel 18: &7Ruinstone (Gilded Blackstone)");
                break;
            case ALCHEMIST:
                if (level == 1)
                    rewards.add("&fLevel Bonus: &7+5% Brewing Speed per level");
                break;
            case EXPLORER:
                if (level == 1) {
                    rewards.add("&fLevel Bonus: &7+0.5% Movement Speed per level");
                    rewards.add("&fLevel Bonus: &7+5 Max Mana per level");
                }
                break;
        }
        return rewards;
    }

    public static List<String> getSpecialInfo(Job job, int level) {
        List<String> info = new ArrayList<>();
        switch (job) {
            case LUMBERJACK:
                info.add("&bRare Drop: &2Special Branch &f(Lvl " + getRareDropLevelRange(level) + ")");
                break;
            case MINER:
                info.add("&bRare Drop: &3Rare Gem &f(Lvl " + getRareDropLevelRange(level) + ")");
                break;
            case ALCHEMIST:
                info.add("&bBonus: &fFaster Brewing &a(+" + (level * 5) + "%)");
                break;
            case EXPLORER:
                double speedPercent = level * 0.5;
                int manaBonus = level * 5;
                info.add("&bBonus: &fPermanent Speed &a(+" + String.format("%.1f", speedPercent) + "%)");
                info.add("&bBonus: &fMax Mana &a(+" + manaBonus + ")");
                break;
        }
        return info;
    }

    private static String getRareDropLevelRange(int jobLevel) {
        int min = 1;
        int max = Math.min(10, (jobLevel / 2) + 1);
        return min + "-" + max;
    }

    public static ItemStack getRareItem(Job job, int itemLevel) {
        Material mat = job == Job.MINER ? Material.AMETHYST_SHARD : Material.DEAD_BUSH;
        String name = job == Job.MINER ? "&3&lRare Gem" : "&2&lSpecial Branch";
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String stars = getStarString(itemLevel);
            meta.setDisplayName(Utils.applyFormat(name));
            List<String> lore = new ArrayList<>();
            lore.add(Utils.applyFormat((job == Job.MINER ? "&b" : "&a") + "A rare finding from your "
                    + Utils.getSentenceCase(job.name()) + " job."));
            lore.add(Utils.applyFormat((job == Job.MINER ? "&b" : "&a") + "- &fCan be sold or used for crafting."));
            lore.add("");
            lore.add(Utils.applyFormat("&d&lRarity&d: " + stars));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String getStarString(int level) {
        int stars = Math.max(1, Math.min(10, level));
        StringBuilder sb = new StringBuilder("&e");
        for (int i = 0; i < stars; i++) {
            sb.append("★");
        }
        for (int i = stars; i < 10; i++) {
            sb.append("&8☆");
        }
        return sb.toString();
    }

    public static ItemStack getJobDrop(Material block, org.bukkit.Location location) {
        // Check if oak-type blocks are broken in Enchanted Lands -> enchanted drop
        if (location != null && isEnchantedLands(location)) {
            switch (block) {
                case OAK_WOOD, SPRUCE_STAIRS, SPRUCE_SLAB, SPRUCE_FENCE:
                    return AnimalQuestItem.ENCHANTED_OAK_LOG.getItemStack(1);
                default:
                    break;
            }
        }
        return getJobDrop(block);
    }

    public static boolean isEnchantedLands(org.bukkit.Location location) {
        ca.pandaaa.animalquest.AnimalQuest plugin = ca.pandaaa.animalquest.AnimalQuest.getPlugin();
        if (plugin.getZoneManager() == null) return false;
        for (ZoneManager.Zone zone : plugin.getZoneManager().getZonesAt(location)) {
            if (zone.getName().contains("Enchanted Lands")) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getJobDrop(Material block) {
        return switch (block) {
            // Lumberjack custom drops -> Normalized to Oak Log
            case OAK_WOOD, SPRUCE_STAIRS, SPRUCE_SLAB, SPRUCE_FENCE -> AnimalQuestItem.OAK_LOG.getItemStack(1);
            case DARK_OAK_WOOD, DARK_OAK_STAIRS, DARK_OAK_SLAB, DARK_OAK_FENCE ->
                AnimalQuestItem.DARK_OAK_LOG.getItemStack(1);
            case CACTUS -> AnimalQuestItem.CACTUS.getItemStack(1);
            case CACTUS_FLOWER -> AnimalQuestItem.CACTUS_FLOWER.getItemStack(1);
            case SPRUCE_WOOD -> AnimalQuestItem.SPRUCE_LOG.getItemStack(1);
            case BIRCH_WOOD -> AnimalQuestItem.BIRCH_LOG.getItemStack(1);
            case PALE_OAK_WOOD, PALE_OAK_STAIRS, PALE_OAK_SLAB, PALE_OAK_FENCE ->
                AnimalQuestItem.PALE_OAK_LOG.getItemStack(1);

            // Miner custom drops -> Special Minerals
            case COBBLESTONE -> AnimalQuestItem.COBBLESTONE.getItemStack(1);
            case COAL_ORE, DEEPSLATE_COAL_ORE -> AnimalQuestItem.COAL.getItemStack(1);
            case DIORITE -> AnimalQuestItem.TITANIUM.getItemStack(1);
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> AnimalQuestItem.GOLD.getItemStack(1);
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> AnimalQuestItem.DIAMOND.getItemStack(1);
            case ANCIENT_DEBRIS -> AnimalQuestItem.METEORITE.getItemStack(1);
            case CRYING_OBSIDIAN -> AnimalQuestItem.DRACONIUM.getItemStack(1);
            case GILDED_BLACKSTONE -> AnimalQuestItem.RUINSTONE.getItemStack(1);

            default -> null; // Use regular drops
        };
    }
}
