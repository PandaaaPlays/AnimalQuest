package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private final AnimalQuest plugin;
    private final File playersFolder;
    private final Map<UUID, PlayerData> loadedPlayers = new ConcurrentHashMap<>();

    public PlayerDataManager(AnimalQuest plugin) {
        this.plugin = plugin;
        this.playersFolder = new File(plugin.getDataFolder(), "players");

        if (!playersFolder.exists())
            playersFolder.mkdirs();

        startAutoSave(300);
    }

    private PlayerData loadPlayer(UUID uuid) {
        return loadedPlayers.computeIfAbsent(uuid, id -> {
            File file = new File(playersFolder, id + ".yml");

            if (!file.exists())
                return new PlayerData(id);

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Map<String, Object> map = new HashMap<>();

            for (String key : config.getKeys(false))
                map.put(key, config.get(key));

            return new PlayerData(map);
        });
    }

    public PlayerData get(UUID uuid) {
        return loadPlayer(uuid);
    }

    private void writeSnapshot(UUID uuid, Map<String, Object> snapshot) {
        File file = new File(playersFolder, uuid + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        snapshot.forEach(config::set);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player " + uuid);
            e.printStackTrace();
        }
    }

    private void asyncWriteSnapshot(UUID uuid, Map<String, Object> snapshot) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> writeSnapshot(uuid, snapshot));
    }

    private void startAutoSave(int seconds) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Snapshot synchronously
            Map<UUID, Map<String, Object>> snapshots = new HashMap<>();
            for (Map.Entry<UUID, PlayerData> entry : loadedPlayers.entrySet()) {
                snapshots.put(entry.getKey(), entry.getValue().serialize());
            }

            // Write asynchronously
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                snapshots.forEach(this::writeSnapshot);
            });
        }, 20L * seconds, 20L * seconds);
    }

    public void unloadPlayer(UUID uuid) {
        PlayerData data = loadedPlayers.remove(uuid);
        if (data == null) return;

        Map<String, Object> snapshot = data.serialize();
        asyncWriteSnapshot(uuid, snapshot);
    }

    public void initialize() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = this.get(player.getUniqueId());
            data.updateManaDisplay(player);

            // Delay setup slightly to ensure everything is initialized and avoid issues during reload
            org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    plugin.getScoreboardManager().setupScoreboard(player);
                    plugin.getScoreboardManager().updateTablist(player);
                    plugin.getScoreboardManager().updateTablistHeader(player);
                }
            }, 1L);
        }
    }

    public void shutdown() {
        // Snapshot synchronously
        Map<UUID, Map<String, Object>> snapshots = new HashMap<>();
        for (Map.Entry<UUID, PlayerData> entry : loadedPlayers.entrySet()) {
            snapshots.put(entry.getKey(), entry.getValue().serialize());
        }

        // Save synchronously (blocking as the server is stopping anyway).
        snapshots.forEach(this::writeSnapshot);

        loadedPlayers.clear();
    }
}
