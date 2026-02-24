package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class ZoneManager implements Listener {

    private record BiomeInfo(String name, String subtitle, int level) {
    }

    private final AnimalQuest plugin;
    private final Map<Biome, BiomeInfo> biomeZones = new HashMap<>();
    private final Map<UUID, String> lastZoneName = new HashMap<>();

    public ZoneManager(AnimalQuest plugin) {
        this.plugin = plugin;
        loadZones();
        startCheckTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void loadZones() {
        File file = new File(plugin.getDataFolder(), "zones.yml");
        if (!file.exists()) {
            plugin.saveResource("zones.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        biomeZones.clear();
        if (config.contains("zones")) {
            for (String key : config.getConfigurationSection("zones").getKeys(false)) {
                String name = config.getString("zones." + key + ".name");
                int level = config.getInt("zones." + key + ".recommended-level");
                String subtitle = config.getString("zones." + key + ".subtitle", "");
                List<String> biomes = config.getStringList("zones." + key + ".biomes");
                for (String bStr : biomes) {
                    NamespacedKey keyPath = NamespacedKey.minecraft(bStr.toLowerCase());
                    Biome biome = Registry.BIOME.get(keyPath);
                    if (biome != null) {
                        biomeZones.put(biome, new BiomeInfo(name, subtitle, level));
                    }
                }
            }
        }
    }

    private void startCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerZone(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void updatePlayerZone(Player player) {
        Location location = player.getLocation();

        Biome biome = location.getBlock().getBiome();
        BiomeInfo info = biomeZones.get(biome);
        if (info == null) {
            lastZoneName.remove(player.getUniqueId());
            return;
        }

        String zoneName = "&3&lEntering " + info.name;
        String subtitle = info.subtitle;

        String lastKnownZoneName = lastZoneName.get(player.getUniqueId());
        if (lastKnownZoneName == null || !lastKnownZoneName.equals(zoneName)) {
            PlayerData playerData = plugin.getPlayerDataManager().get(player.getUniqueId());
            if (playerData.getExperience().getLevel() < info.level) {
                subtitle = "&cRecommended level: &4" + info.level;
            }
            if (subtitle.isEmpty()) {
                subtitle = "&aRecommended level: &2" + info.level;
            }
            sendZoneTitle(player, zoneName, subtitle);
            lastZoneName.put(player.getUniqueId(), zoneName);
        }
    }

    private void sendZoneTitle(Player player, String title, String subtitle) {
        player.sendTitle(Utils.applyFormat(title), Utils.applyFormat(subtitle), 10, 80, 10);
    }

    @org.bukkit.event.EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        lastZoneName.remove(event.getPlayer().getUniqueId());
    }
}
