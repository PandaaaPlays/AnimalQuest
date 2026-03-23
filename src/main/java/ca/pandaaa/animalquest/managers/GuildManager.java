package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.Guild;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuildManager {
    private final AnimalQuest plugin;
    private final Map<String, Guild> guilds = new HashMap<>();
    private final Map<UUID, String> playerGuild = new HashMap<>();
    private final Map<UUID, String> guildInvites = new HashMap<>();
    private final File guildFile;
    private FileConfiguration guildConfig;

    public GuildManager(AnimalQuest plugin) {
        this.plugin = plugin;
        this.guildFile = new File(plugin.getDataFolder(), "guilds.yml");
        loadGuilds();
    }

    private void loadGuilds() {
        if (!guildFile.exists()) {
            plugin.saveResource("guilds.yml", false);
        }
        guildConfig = YamlConfiguration.loadConfiguration(guildFile);
        for (String key : guildConfig.getKeys(false)) {
            Guild guild = (Guild) guildConfig.get(key);
            if (guild != null) {
                guilds.put(guild.getName().toLowerCase(), guild);
                for (UUID member : guild.getMembers()) {
                    playerGuild.put(member, guild.getName().toLowerCase());
                }
            }
        }
    }

    public void saveGuilds() {
        for (Guild guild : guilds.values()) {
            guildConfig.set(guild.getName().toLowerCase(), guild);
        }
        try {
            guildConfig.save(guildFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save guilds.yml!");
        }
    }

    public Guild getGuild(String name) {
        return guilds.get(name.toLowerCase());
    }

    public Guild getPlayerGuild(UUID uuid) {
        String guildName = playerGuild.get(uuid);
        return guildName != null ? guilds.get(guildName) : null;
    }

    public void createGuild(String name, String tag, UUID owner) {
        Guild guild = new Guild(name, tag, owner);
        guilds.put(name.toLowerCase(), guild);
        playerGuild.put(owner, name.toLowerCase());
        saveGuilds();
    }

    public void deleteGuild(String name) {
        Guild guild = guilds.remove(name.toLowerCase());
        if (guild != null) {
            for (UUID member : guild.getMembers()) {
                playerGuild.remove(member);
            }
            guildConfig.set(name.toLowerCase(), null);
            saveGuilds();
        }
    }

    public void joinGuild(UUID uuid, String guildName) {
        Guild guild = getGuild(guildName);
        if (guild != null) {
            guild.addMember(uuid);
            playerGuild.put(uuid, guildName.toLowerCase());
            guildInvites.remove(uuid);
            saveGuilds();
        }
    }

    public void invitePlayer(UUID inviterUuid, UUID targetUuid) {
        Guild guild = getPlayerGuild(inviterUuid);
        if (guild != null) {
            guildInvites.put(targetUuid, guild.getName().toLowerCase());
        }
    }

    public String getInvite(UUID uuid) {
        return guildInvites.get(uuid);
    }

    public void removeInvite(UUID uuid) {
        guildInvites.remove(uuid);
    }

    public void leaveGuild(UUID uuid) {
        String guildName = playerGuild.remove(uuid);
        if (guildName != null) {
            Guild guild = getGuild(guildName);
            if (guild != null) {
                guild.removeMember(uuid);
                if (guild.getMembers().isEmpty()) {
                    deleteGuild(guildName);
                } else {
                    saveGuilds();
                }
            }
        }
    }

    public boolean guildExists(String name) {
        return guilds.containsKey(name.toLowerCase());
    }
}
