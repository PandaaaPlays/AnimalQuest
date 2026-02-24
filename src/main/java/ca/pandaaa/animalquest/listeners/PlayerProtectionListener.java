package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerProtectionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        // TODO, allow breaking of certain blocks in certain zones
        if (!player.hasPermission("animalquest.admin")) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot break this block here!"));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("animalquest.admin")) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot place this block here!"));
        }
    }

    @EventHandler
    public void onContainerOpening(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (!player.hasPermission("animalquest.admin")) {
            InventoryType type = event.getInventory().getType();
            if (type == InventoryType.ANVIL || type == InventoryType.BARREL || type == InventoryType.BLAST_FURNACE
                    || type == InventoryType.BREWING || type == InventoryType.WORKBENCH) {
                event.setCancelled(true);
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot open this here!"));
            }
        }
    }

    @EventHandler
    public void onFluidFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("animalquest.admin")) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot use this here!"));
        }
    }

    @EventHandler
    public void onFluidPlacement(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("animalquest.admin")) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot use this here!"));
        }
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {
        Player player = event.getPlayer();

        assert player != null;
        if (!player.hasPermission("animalquest.admin")) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot use this here!"));
        }
    }

    @EventHandler
    public void stripEvent(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("animalquest.admin"))
            return;

        if (event.getClickedBlock() == null)
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getClickedBlock().getType() == Material.GRASS_BLOCK
                || event.getClickedBlock().getType() == Material.DIRT) {
            if (event.getMaterial().toString().contains("_SHOVEL")) {
                event.setCancelled(true);
            }
        }

        if (event.getClickedBlock().getType().toString().contains("LOG")
                || event.getClickedBlock().getType().toString().contains("WOOD")) {
            if (event.getMaterial().toString().contains("_AXE")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCraft(CraftItemEvent event) {
        event.setCancelled(true);
        event.getWhoClicked().sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot craft on this server."));
    }
}
