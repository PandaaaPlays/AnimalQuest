package ca.pandaaa.animalquest.player;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> loadedPlayers = new HashMap<>();
    private final File playerFolder;

    public PlayerDataManager(AnimalQuest plugin) {
        this.playerFolder = new File(plugin.getDataFolder(), "players");
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }
    }

    public PlayerData loadPlayer(UUID uuid) {
        if (loadedPlayers.containsKey(uuid)) {
            return loadedPlayers.get(uuid);
        }

        File file = new File(playerFolder, uuid.toString() + ".yml");
        PlayerData data;

        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            Map<String, Object> map = new HashMap<>();
            for (String key : config.getKeys(false)) {
                map.put(key, config.get(key));
            }

            data = new PlayerData(map);
        } else {
            data = new PlayerData(uuid);
        }

        loadedPlayers.put(uuid, data);
        return data;
    }


    public void savePlayer(UUID uuid) {
        PlayerData data = loadedPlayers.get(uuid);
        if (data == null) return;

        File file = new File(playerFolder, uuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        Map<String, Object> serialized = data.serialize();

        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void unloadPlayer(UUID uuid) {
        savePlayer(uuid);
        loadedPlayers.remove(uuid);
    }

    public void saveAll() {
        for (UUID uuid : loadedPlayers.keySet()) {
            savePlayer(uuid);
        }
    }

    public PlayerData get(UUID uuid) {
        return loadedPlayers.get(uuid);
    }
}
