package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        PlayerData data = playerDataManager.loadPlayer(player.getUniqueId());

        data.applyAptitudes(player);
        scoreboardManager.setupScoreboard(player);

        scoreboardManager.updatePlayerTablistDisplay(player);
        scoreboardManager.updateTablistHeader();
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
