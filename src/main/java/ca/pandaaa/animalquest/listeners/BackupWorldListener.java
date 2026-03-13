package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.WorldResetManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import ca.pandaaa.animalquest.player.PlayerData;

public class BackupWorldListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (AnimalQuest.getPlugin().getWorldResetManager().isResetInProgress()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    Utils.applyFormat(
                            "&c&l[!] &cServer is currently resetting the world.\n&7Please try again in a few seconds!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        Location to = event.getTo();
        if (to == null)
            return;

        Player player = event.getPlayer();
        if (hasStaffPermission(player)) {
            return;
        }

        if (isRestrictedWorld(to.getWorld())) {
            event.setCancelled(true);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cThis area is restricted."));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (hasStaffPermission(player)) {
            sendStaffWarning(player);
            return;
        }

        if (isRestrictedWorld(player.getWorld())) {
            redirectToPlayerWorld(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (hasStaffPermission(player)) {
            if (player.getWorld().getName().equals(WorldResetManager.BACKUP_WORLD_NAME)) {
                return;
            }
        }

        redirectToPlayerWorld(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data != null) {
            event.setRespawnLocation(data.getHome());
        }
    }

    private boolean isRestrictedWorld(World world) {
        if (world == null)
            return false;

        if (WorldResetManager.BACKUP_WORLD_NAME.equals(world.getName()))
            return true;

        World primaryWorld = AnimalQuest.getPlugin().getServer().getWorlds().get(0);
        return world.equals(primaryWorld);
    }

    private void sendStaffWarning(Player player) {
        if (player.getWorld().getName().equals(WorldResetManager.PLAYER_WORLD_NAME)) {
            player.sendMessage(Utils.applyFormat(
                    "&c&l[!] &cWarning: You are in the player world. Changes here will NOT be saved to the backup! (Use /worldreset switchto backup)"));
        }
    }

    private boolean hasStaffPermission(Player player) {
        return player.hasPermission("animalquest.admin") || player.hasPermission("animalquest.builder");
    }

    private void redirectToPlayerWorld(Player player) {
        Location playerLocation = player.getLocation();
        World playerWorld = player.getServer().getWorld(WorldResetManager.PLAYER_WORLD_NAME);

        if (playerWorld == null || isRestrictedWorld(playerWorld)) {
            player.kickPlayer(Utils.applyFormat("&c&l[!] &cThe game world is currently unavailable."));
            return;
        }

        player.teleport(new Location(playerWorld, playerLocation.getX(), playerLocation.getY(),
                playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
    }
}
