package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionBarManager {
    private final AnimalQuest plugin;
    private final Map<UUID, Long> priorityExpiry = new HashMap<>();
    private final Map<UUID, String> priorityMessages = new HashMap<>();
    private final Map<UUID, String> fallbackMessages = new HashMap<>();

    public ActionBarManager(AnimalQuest plugin) {
        this.plugin = plugin;
        startUpdateTask();
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                String messageToShow = null;

                // Check Priority First
                Long expiry = priorityExpiry.get(uuid);
                if (expiry != null) {
                    if (System.currentTimeMillis() < expiry) {
                        messageToShow = priorityMessages.get(uuid);
                    } else {
                        priorityExpiry.remove(uuid);
                        priorityMessages.remove(uuid);
                    }
                }

                // Fallback to Quest message if no priority
                if (messageToShow == null) {
                    messageToShow = fallbackMessages.get(uuid);
                }

                if (messageToShow != null) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.applyFormat(messageToShow)));
                }
            }
        }, 10L, 2L);
    }

    public void sendPriorityMessage(Player player, String message, int seconds) {
        UUID uuid = player.getUniqueId();
        priorityMessages.put(uuid, message);
        priorityExpiry.put(uuid, System.currentTimeMillis() + (seconds * 1000L));
    }

    public void setFallbackMessage(Player player, String message) {
        if (message == null) {
            fallbackMessages.remove(player.getUniqueId());
        } else {
            fallbackMessages.put(player.getUniqueId(), message);
        }
    }

    public boolean hasPriority(Player player) {
        Long expiry = priorityExpiry.get(player.getUniqueId());
        return expiry != null && System.currentTimeMillis() < expiry;
    }
}

