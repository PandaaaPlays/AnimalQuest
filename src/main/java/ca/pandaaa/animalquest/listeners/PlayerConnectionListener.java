package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.managers.ScoreboardManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final ScoreboardManager scoreboardManager;

    public PlayerConnectionListener(PlayerDataManager playerDataManager, ScoreboardManager scoreboard) {
        this.playerDataManager = playerDataManager;
        this.scoreboardManager = scoreboard;
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        PlayerData data = playerDataManager.get(player.getUniqueId());

        if (!player.hasPlayedBefore()) {
            player.teleport(data.getHome());
            Bukkit.broadcastMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> "
                    + "&b&lWelcome &3" + player.getName() + " &bto the server! &bWe hope you enjoy your stay."));
        }

        data.applyHealthAptitude();

        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            if (player.isOnline()) {
                data.updateManaDisplay();
                scoreboardManager.setupScoreboard(player);
                scoreboardManager.updateTablist(player);
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                scoreboardManager.updateTablistHeader(onlinePlayer);
            }
        }, 1L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        scoreboardManager.setupScoreboard(player);
        scoreboardManager.updateTablist(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateTablistHeader(onlinePlayer);
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        playerDataManager.unloadPlayer(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                scoreboardManager.updateTablistHeader(onlinePlayer);
            }
        }, 1L);
    }
}
