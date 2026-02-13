package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.Guild;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.managers.StaffManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final StaffManager staffManager;

    public ChatListener(PlayerDataManager playerDataManager, StaffManager staffManager) {
        this.playerDataManager = playerDataManager;
        this.staffManager = staffManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Handle StaffChat
        if (staffManager.playerHasStaffChatToggled(player)) {
            event.setCancelled(true);
            staffManager.sendStaffChatMessage(player, message);
            return;
        }

        // Standard Chat Formatting
        PlayerData data = playerDataManager.get(player.getUniqueId());
        if (data == null)
            return;

        String rankPrefix = Utils.getRankPrefix(player);
        String level = Utils
            .applyFormat("&8[" + data.getExperience().getLevelColor() + data.getExperience().getLevel() + "&8]");

        // Guild Tag support
        String guildTag = "";
        Guild guild = AnimalQuest.getPlugin().getGuildManager().getPlayerGuild(player.getUniqueId());
        if (guild != null) {
            guildTag = Utils.applyFormat("&8[&7" + guild.getTag() + "&8] ");
        }

        String format = Utils.applyFormat(guildTag + rankPrefix + "&f" + player.getName() + " " + level + " &8» &f")
            + message;
        event.setFormat(format.replace("%", "%%")); // Prevent player from using color codes in message if desired, or andle accordingly
    }
}
