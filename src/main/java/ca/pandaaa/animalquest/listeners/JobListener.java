package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.managers.JobRewardManager;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.jobs.JobProgress;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JobListener implements Listener {

    private final PlayerDataManager dataManager;
    private final Random random = new Random();
    private final Map<Material, JobXPInfo> jobBlocks = new HashMap<>();

    public JobListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
        initializeBlocks();
    }

    private void initializeBlocks() {
        // Lumberjack
        register(Job.LUMBERJACK, 1, 5, Material.OAK_LOG, Material.BIRCH_LOG, Material.OAK_WOOD, Material.BIRCH_WOOD);
        register(Job.LUMBERJACK, 5, 6, Material.SPRUCE_LOG, Material.JUNGLE_LOG, Material.SPRUCE_WOOD,
                Material.JUNGLE_WOOD);
        register(Job.LUMBERJACK, 10, 8, Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.ACACIA_WOOD,
                Material.DARK_OAK_WOOD);
        register(Job.LUMBERJACK, 15, 10, Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.MANGROVE_WOOD,
                Material.CHERRY_WOOD);
        register(Job.LUMBERJACK, 20, 15, Material.CRIMSON_STEM, Material.WARPED_STEM);

        // Miner
        register(Job.MINER, 1, 1, Material.STONE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE);
        register(Job.MINER, 5, 3, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.COPPER_ORE,
                Material.DEEPSLATE_COPPER_ORE);
        register(Job.MINER, 10, 5, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.LAPIS_ORE,
                Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_GOLD_ORE);
        register(Job.MINER, 15, 8, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DIAMOND_ORE,
                Material.DEEPSLATE_DIAMOND_ORE);
        register(Job.MINER, 20, 15, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.ANCIENT_DEBRIS);
    }

    private void register(Job job, int requiredLevel, double xp, Material... materials) {
        for (Material material : materials) {
            jobBlocks.put(material, new JobXPInfo(job, requiredLevel, xp));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        JobXPInfo info = jobBlocks.get(block.getType());

        if (info == null)
            return;

        PlayerData data = dataManager.get(player.getUniqueId());
        JobProgress progress = data.getJobs().getJob(info.job);

        // Check if level is high enough
        if (progress.getLevel() < info.requiredLevel) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must be " + info.job.name().toLowerCase() + " level "
                    + info.requiredLevel + " to mine this!"));
            return;
        }

        // Give XP
        progress.addExperience(info.xp);

        // Rare Drop Logic
        handleRareDrops(player, info.job, progress.getLevel(), block);
    }

    private void handleRareDrops(Player player, Job job, int jobLevel, Block block) {
        double chance = 0.02; // 2% base chance
        if (random.nextDouble() < chance) {
            int maxItemLvl = Math.min(10, (jobLevel / 2) + 1);
            int itemLvl = random.nextInt(maxItemLvl) + 1;

            // Bias towards higher levels as job level increases
            if (jobLevel > 15 && random.nextBoolean()) {
                itemLvl = Math.min(10, itemLvl + random.nextInt(3));
            }

            player.getInventory().addItem(JobRewardManager.getRareItem(job, itemLvl));
            player.sendMessage(
                    Utils.applyFormat("&6&l[!] &eYou found a rare " + (job == Job.MINER ? "rock" : "twig") + "!"));
        }
    }

    private static class JobXPInfo {
        final Job job;
        final int requiredLevel;
        final double xp;

        JobXPInfo(Job job, int requiredLevel, double xp) {
            this.job = job;
            this.requiredLevel = requiredLevel;
            this.xp = xp;
        }
    }
}
