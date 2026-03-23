package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.CustomMobManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.custommobs.custommobs.Events.CustomMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CustomMobListener implements Listener {
    private final AnimalQuest plugin;

    public CustomMobListener(AnimalQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCustomMobDeath(CustomMobDeathEvent event) {
        Player killer = event.getOriginalEntityDeathEvent().getEntity().getKiller();
        if (killer == null)
            return;

        String mobId = event.getCustomMob().getCustomMobFileName().replace(".yml", "");
        PlayerData data = plugin.getPlayerDataManager().get(killer.getUniqueId());
        if (data == null)
            return;

        // Log statistic
        data.getStatistics().addKill(mobId);

        // Handle rewards
        CustomMobManager.CustomMobReward reward = plugin.getCustomMobManager().getReward(mobId);
        if (reward != null) {
            if (reward.experience() > 0) {
                data.getExperience().addExperience(reward.experience());
                plugin.getActionBarManager().sendPriorityMessage(killer, "&a&l+ " + reward.experience() + " xp", 3);
            }
            if (!reward.messages().isEmpty()) {
                for (String message : reward.messages()) {
                    killer.sendMessage(Utils.applyFormat(message));
                }
            }
        }
    }
}
