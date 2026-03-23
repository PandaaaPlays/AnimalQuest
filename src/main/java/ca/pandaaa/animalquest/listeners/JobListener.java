package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.guis.LumberjackCraftingGUI;
import ca.pandaaa.animalquest.guis.MinerSmeltingGUI;
import ca.pandaaa.animalquest.managers.JobRewardManager;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.jobs.JobProgress;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class JobListener implements Listener {

    private final PlayerDataManager dataManager;
    private final Random random = new Random();
    private final Map<Material, JobXPInfo> jobBlocks = new HashMap<>();
    private final Map<UUID, Double> explorerDistance = new HashMap<>();

    public JobListener(PlayerDataManager dataManager) {
        this.dataManager = dataManager;
        initializeBlocks();
    }

    private void initializeBlocks() {
        // Lumberjack
        register(Job.LUMBERJACK, 1, 2, Material.OAK_WOOD, Material.SPRUCE_STAIRS, Material.SPRUCE_SLAB,
                Material.SPRUCE_FENCE);
        register(Job.LUMBERJACK, 3, 5, Material.DARK_OAK_WOOD, Material.DARK_OAK_STAIRS, Material.DARK_OAK_SLAB,
                Material.DARK_OAK_FENCE);
        register(Job.LUMBERJACK, 5, 10, Material.CACTUS);
        register(Job.LUMBERJACK, 7, 30, Material.CACTUS_FLOWER);
        register(Job.LUMBERJACK, 10, 20, Material.SPRUCE_WOOD);
        register(Job.LUMBERJACK, 15, 35, Material.BIRCH_WOOD);
        register(Job.LUMBERJACK, 18, 50, Material.PALE_OAK_WOOD);

        // Miner
        register(Job.MINER, 1, 1, Material.COBBLESTONE);
        register(Job.MINER, 2, 3, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE);
        register(Job.MINER, 3, 4, Material.DIORITE);
        register(Job.MINER, 5, 10, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
        register(Job.MINER, 8, 15, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE);
        register(Job.MINER, 10, 20, Material.ANCIENT_DEBRIS);
        register(Job.MINER, 15, 35, Material.CRYING_OBSIDIAN);
        register(Job.MINER, 18, 50, Material.GILDED_BLACKSTONE);
    }

    private void register(Job job, int requiredLevel, int xp, Material... materials) {
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

        if (progress.getLevel() < info.requiredLevel) {
            event.setCancelled(true);
            player.sendMessage(
                    Utils.applyFormat("&c&l[!] &cYou must be " + Utils.getSentenceCase(info.job.name()) + " level "
                            + info.requiredLevel + " to mine this!"));
            return;
        }

        boolean inEnchantedLandsTree = JobRewardManager.isEnchantedLands(block.getLocation())
                && info.job == Job.LUMBERJACK;
        int xpGained = inEnchantedLandsTree ? info.xp + 1 : info.xp;

        progress.addExperience(xpGained);
        String xpMsg = "&3&l[ &b+" + xpGained + " " + Utils.getSentenceCase(info.job.name()) + " xp &3&l]";
        AnimalQuest.getPlugin().getActionBarManager().sendPriorityMessage(player, xpMsg, 2);

        Integer regenTime = AnimalQuest.getPlugin().getZoneManager().getRegenTime(block.getLocation(), block.getType());
        if (regenTime != null) {
            AnimalQuest.getPlugin().getBlockRegenManager().queueRegen(block, regenTime);
        }

        ItemStack customDrop = JobRewardManager.getJobDrop(block.getType(), block.getLocation());
        if (customDrop != null) {
            event.setDropItems(false);
            block.getWorld().dropItemNaturally(block.getLocation(), customDrop);
        }

        handleRareDrops(player, info.job, block, progress.getLevel(), info.requiredLevel);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null)
            return;

        Player player = event.getPlayer();
        Material blockType = event.getClickedBlock().getType();
        PlayerData data = dataManager.get(player.getUniqueId());
        if (data == null)
            return;

        if (blockType == Material.CRAFTER) {
            event.setCancelled(true);
            new LumberjackCraftingGUI(player).open();
        } else if (blockType == Material.BLAST_FURNACE) {
            event.setCancelled(true);
            new MinerSmeltingGUI(player).open();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double distance = from.distance(to);

        double totalDistance = explorerDistance.getOrDefault(uuid, 0.0) + distance;
        if (totalDistance >= 250.0) {
            PlayerData data = dataManager.get(uuid);
            JobProgress progress = data.getJobs().getExplorer();
            int xp = (int) (totalDistance / 25.0);
            progress.addExperience(xp);
            AnimalQuest.getPlugin().getActionBarManager().sendPriorityMessage(player,
                    "&3&l[ &b+" + xp + " " + Utils.getSentenceCase(Job.EXPLORER.name()) + " xp &3&l]", 2);

            totalDistance %= 250.0;
        }
        explorerDistance.put(uuid, totalDistance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        explorerDistance.remove(event.getPlayer().getUniqueId());
    }

    private void handleRareDrops(Player player, Job job, Block block, int jobLevel, int requiredLevel) {
        // Chance of finding a rare item (2%)
        double chance = 0.02;
        if (random.nextDouble() < chance) {
            // Base rarity: Level 18 gets 9, Level 20 gets 10 as base potential.
            int baseMax = Math.max(1, jobLevel / 2);
            int itemLvl = random.nextInt(baseMax) + 1;

            // DIFFICULTY BONUS: Tougher blocks push the rarity higher.
            int difficultyBonus = (requiredLevel / 2) + 1;
            int difficultyRoll = random.nextInt(difficultyBonus) + 1;
            itemLvl = Math.max(itemLvl, difficultyRoll);

            itemLvl = Math.max(1, Math.min(10, itemLvl));

            block.getWorld().dropItemNaturally(block.getLocation(), JobRewardManager.getRareItem(job, itemLvl));
            player.sendMessage(Utils.applyFormat(
                    "&b&l[!] &bYou found a " + (job == Job.MINER ? "rare gem" : "special branch") + "!"));
        }
    }

    private static class JobXPInfo {
        final Job job;
        final int requiredLevel;
        final int xp;

        JobXPInfo(Job job, int requiredLevel, int xp) {
            this.job = job;
            this.requiredLevel = requiredLevel;
            this.xp = xp;
        }
    }
}
