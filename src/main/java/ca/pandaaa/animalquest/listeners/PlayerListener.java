package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.managers.ScoreboardManager;
import ca.pandaaa.animalquest.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final ScoreboardManager scoreboardManager;

    public PlayerListener(PlayerDataManager playerDataManager, ScoreboardManager scoreboard) {
        this.playerDataManager = playerDataManager;
        this.scoreboardManager = scoreboard;
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = playerDataManager.get(player.getUniqueId());

        data.applyAptitudes(player);

        org.bukkit.Bukkit.getScheduler().runTaskLater(ca.pandaaa.animalquest.AnimalQuest.getPlugin(), () -> {
            if (player.isOnline()) {
                scoreboardManager.setupScoreboard(player);
                scoreboardManager.updateTablist(player);
                scoreboardManager.updateTablistHeader(player);
            }
        }, 1L);
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerDataManager.unloadPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onExpBottleThrowEvent(ExpBottleEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            PlayerData data = playerDataManager.get(player.getUniqueId());
            if (data != null) {
                data.getMana().addMana(5.0);
                event.setExperience(0);
            }
        }
    }
}
