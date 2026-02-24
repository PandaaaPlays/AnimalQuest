package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffManager {
    private final Set<UUID> staffChatToggled = new HashSet<>();

    public void toggleStaffChat(Player player) {
        if (staffChatToggled.contains(player.getUniqueId())) {
            staffChatToggled.remove(player.getUniqueId());
            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bStaff Chat &coff&b."));
        } else {
            staffChatToggled.add(player.getUniqueId());
            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bStaff Chat &aon&b."));
        }
    }

    public boolean playerHasStaffChatToggled(Player player) {
        return staffChatToggled.contains(player.getUniqueId());
    }

    public void sendStaffChatMessage(Player sender, String message) {
        String formattedMessage = Utils.applyFormat("&8[&c&lSTAFF&8] &b" + sender.getName() + " &7&l>> &f" + message);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("animalquest.staff")) {
                onlinePlayer.sendMessage(formattedMessage);
            }
        }
        Bukkit.getLogger().info("[STAFF] " + sender.getName() + ": " + message);
    }

    public void broadcast(String message) {
        String formattedMessage = Utils.applyFormat("&r\n&8[&b&lBROADCAST&8] &f" + message + "\n&r");
        Bukkit.broadcastMessage(formattedMessage);
    }
}
