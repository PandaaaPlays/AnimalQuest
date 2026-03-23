package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, Long> cooldown = new HashMap<>();

    public HomeManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void teleportToHome(Player player) {
        long remaining = getRemainingCooldown(player);
        if (remaining > 0) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cTeleportation on cooldown! (" + remaining + "s)"));
            return;
        }

        cooldown.put(player.getUniqueId(), System.currentTimeMillis());
        player.teleport(playerDataManager.get(player.getUniqueId()).getHome());
    }

    public long getRemainingCooldown(Player player) {
        Long playerCooldown = cooldown.get(player.getUniqueId());
        if (playerCooldown == null)
            return 0;
        long timeSinceCast = System.currentTimeMillis() - playerCooldown;
        long cooldownMillis = 5 * 60 * 1000L; // 5 Minutes

        if (player.hasPermission("animalquest.admin"))
            cooldownMillis = 5 * 1000L; // 5 Seconds

        if (timeSinceCast >= cooldownMillis)
            return 0;

        return (cooldownMillis - timeSinceCast) / 1000L + 1;
    }
}
