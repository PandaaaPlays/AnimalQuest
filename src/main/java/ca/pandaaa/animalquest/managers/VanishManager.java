package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager implements Listener {
    private final AnimalQuest plugin;
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishManager(AnimalQuest plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void setVanished(Player player, boolean vanished) {
        if (vanished) {
            vanishedPlayers.add(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("animalquest.staff")) {
                    onlinePlayer.hidePlayer(plugin, player);
                }
            }
        } else {
            vanishedPlayers.remove(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
        }
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("animalquest.staff")) {
            for (UUID vanishedId : vanishedPlayers) {
                Player vanishedPlayer = Bukkit.getPlayer(vanishedId);
                if (vanishedPlayer != null) {
                    player.hidePlayer(plugin, vanishedPlayer);
                }
            }
        }
    }
}
