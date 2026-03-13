package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class NPCManager implements Listener {
    private final AnimalQuest plugin;
    private final NamespacedKey npcTypeKey;
    private final NamespacedKey npcSubtypeKey;

    private final java.util.Map<java.util.UUID, String> pendingHomes = new java.util.HashMap<>();
    private final java.util.Map<java.util.UUID, String> pendingMounts = new java.util.HashMap<>();

    private File file;
    private FileConfiguration config;

    public NPCManager(AnimalQuest plugin) {
        this.plugin = plugin;
        this.npcTypeKey = new NamespacedKey(plugin, "npc_type");
        this.npcSubtypeKey = new NamespacedKey(plugin, "npc_subtype");

        loadConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void loadConfig() {
        file = new File(plugin.getDataFolder(), "npcs.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("npcs.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void addPendingHome(java.util.UUID uuid, String home) {
        pendingHomes.put(uuid, home);
    }

    public boolean consumePendingHome(java.util.UUID uuid, String home) {
        if (home.equalsIgnoreCase(pendingHomes.get(uuid))) {
            pendingHomes.remove(uuid);
            return true;
        }
        return false;
    }

    public void addPendingMount(java.util.UUID uuid, String mount) {
        pendingMounts.put(uuid, mount);
    }

    public boolean consumePendingMount(java.util.UUID uuid, String mount) {
        if (mount.equalsIgnoreCase(pendingMounts.get(uuid))) {
            pendingMounts.remove(uuid);
            return true;
        }
        return false;
    }

    public void resetNpcs() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntitiesByClass(org.bukkit.entity.LivingEntity.class)) {
                if (entity.getPersistentDataContainer().has(npcTypeKey, PersistentDataType.STRING) ||
                        entity.getPersistentDataContainer().has(new NamespacedKey(plugin, "shop"),
                                PersistentDataType.STRING)) {
                    entity.remove();
                }
            }
        }

        ConfigurationSection npcsSection = config.getConfigurationSection("npcs");
        if (npcsSection != null) {
            for (String key : npcsSection.getKeys(false)) {
                spawnNpcFromConfig(npcsSection.getConfigurationSection(key));
            }
        }
    }

    private void spawnNpcFromConfig(ConfigurationSection section) {
        if (section == null)
            return;

        String type = section.getString("type");
        String subtype = section.getString("subtype");
        String name = section.getString("name");
        String locationStr = section.getString("location");

        if (type == null || subtype == null || locationStr == null)
            return;

        Location loc = parseLocation(locationStr);
        if (loc == null || loc.getWorld() == null)
            return;

        String entityTypeStr = section.getString("entity_type");
        org.bukkit.entity.EntityType entityType = org.bukkit.entity.EntityType.VILLAGER;
        if (entityTypeStr != null) {
            try {
                entityType = org.bukkit.entity.EntityType.valueOf(entityTypeStr.toUpperCase());
            } catch (Exception ignored) {
            }
        }

        org.bukkit.entity.LivingEntity npc = (org.bukkit.entity.LivingEntity) loc.getWorld().spawnEntity(loc,
                entityType);

        String displayName = type;
        if (name != null) {
            displayName = name;
        } else if (type.equalsIgnoreCase("SHOP")) {
            ca.pandaaa.animalquest.shop.Shop shop = plugin.getShopManager().getShop(subtype);
            if (shop != null) {
                displayName = shop.getName();
            }
        }

        if (npc instanceof Villager) {
            Villager villager = (Villager) npc;
            String villagerTypeStr = section.getString("villager_type");
            String professionStr = section.getString("profession");

            if (villagerTypeStr != null) {
                try {
                    org.bukkit.entity.Villager.Type vType = org.bukkit.Registry.VILLAGER_TYPE
                            .get(org.bukkit.NamespacedKey.minecraft(villagerTypeStr.toLowerCase()));
                    if (vType != null) {
                        villager.setVillagerType(vType);
                    }
                } catch (Exception ignored) {
                }
            }

            if (professionStr != null) {
                try {
                    org.bukkit.entity.Villager.Profession vProf = org.bukkit.Registry.VILLAGER_PROFESSION
                            .get(org.bukkit.NamespacedKey.minecraft(professionStr.toLowerCase()));
                    if (vProf != null) {
                        villager.setProfession(vProf);
                    } else {
                        villager.setProfession(org.bukkit.entity.Villager.Profession.NITWIT);
                    }
                } catch (Exception ignored) {
                    villager.setProfession(org.bukkit.entity.Villager.Profession.NITWIT);
                }
            } else {
                villager.setProfession(org.bukkit.entity.Villager.Profession.NITWIT);
            }
        }

        boolean immobile = section.getBoolean("immobile", true);

        if (type.equalsIgnoreCase("MOUNT") && npc instanceof org.bukkit.entity.Horse) {
            try {
                ca.pandaaa.animalquest.enums.MountType mountType = ca.pandaaa.animalquest.enums.MountType
                        .valueOf(subtype.toUpperCase());
                org.bukkit.entity.Horse horse = (org.bukkit.entity.Horse) npc;
                horse.setTamed(true);
                horse.getInventory().setSaddle(new org.bukkit.inventory.ItemStack(org.bukkit.Material.SADDLE));
                if (mountType.getColor() != null)
                    horse.setColor(mountType.getColor());
                if (mountType.getStyle() != null)
                    horse.setStyle(mountType.getStyle());
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (immobile) {
            npc.setAI(false);
            npc.setCollidable(false);
        } else {
            npc.setAI(true);
            npc.setCollidable(true);
            if (npc instanceof org.bukkit.entity.Ageable) {
                ((org.bukkit.entity.Ageable) npc).setAdult();
            }
        }

        npc.setRemoveWhenFarAway(false);
        npc.setInvulnerable(true);
        npc.setSilent(true);

        if (npc instanceof org.bukkit.entity.Tameable) {
            ((org.bukkit.entity.Tameable) npc).setTamed(true);
        }

        npc.getPersistentDataContainer().set(npcTypeKey, PersistentDataType.STRING, type.toUpperCase());
        npc.getPersistentDataContainer().set(npcSubtypeKey, PersistentDataType.STRING, subtype);

        if (immobile) {
            org.bukkit.entity.ArmorStand hologram = (org.bukkit.entity.ArmorStand) loc.getWorld().spawnEntity(
                    loc.clone().add(0, 2.25, 0), org.bukkit.entity.EntityType.ARMOR_STAND);
            hologram.setInvisible(true);
            hologram.setMarker(true);
            hologram.setCustomName(Utils.applyFormat(displayName));
            hologram.setCustomNameVisible(true);
            hologram.getPersistentDataContainer().set(npcTypeKey, PersistentDataType.STRING, type.toUpperCase());

            org.bukkit.entity.ArmorStand hologramSubtitle = (org.bukkit.entity.ArmorStand) loc.getWorld().spawnEntity(
                    loc.clone().add(0, 2.0, 0), org.bukkit.entity.EntityType.ARMOR_STAND);
            hologramSubtitle.setInvisible(true);
            hologramSubtitle.setMarker(true);
            hologramSubtitle.setCustomName(Utils.applyFormat("&7( Click to interact )"));
            hologramSubtitle.setCustomNameVisible(true);
            hologramSubtitle.getPersistentDataContainer().set(npcTypeKey, PersistentDataType.STRING,
                    type.toUpperCase());
        }
    }

    private Location parseLocation(String str) {
        String[] parts = str.split(";");
        if (parts.length < 4)
            return null;
        try {
            World world = Bukkit.getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = parts.length > 4 ? Float.parseFloat(parts[4]) : 0f;
            float pitch = parts.length > 5 ? Float.parseFloat(parts[5]) : 0f;
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }

    @EventHandler
    public void onNpcClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!entity.getPersistentDataContainer().has(npcTypeKey, PersistentDataType.STRING))
            return;

        event.setCancelled(true);

        String type = entity.getPersistentDataContainer().get(npcTypeKey, PersistentDataType.STRING);
        String subtype = entity.getPersistentDataContainer().get(npcSubtypeKey, PersistentDataType.STRING);

        if (type == null || subtype == null)
            return;

        if (type.equalsIgnoreCase("SHOP")) {
            plugin.getShopManager().openShop(event.getPlayer(), subtype);
        } else if (type.equalsIgnoreCase("HOME")) {
            PlayerData data = plugin.getPlayerDataManager().get(event.getPlayer().getUniqueId());
            if (data != null) {
                if (data.getHomeName().equalsIgnoreCase(subtype)) {
                    event.getPlayer().sendMessage(Utils.applyFormat("&c&l[!] &cYour home is already set here!"));
                } else {
                    addPendingHome(event.getPlayer().getUniqueId(), subtype);
                    net.md_5.bungee.api.chat.BaseComponent[] homeMsg = net.md_5.bungee.api.chat.TextComponent
                            .fromLegacyText(
                                    Utils.applyFormat(
                                            Utils.getAnimalQuestName() + " &7&l>> &fClick here to set your home to &b"
                                                    + Utils.getSentenceCase(subtype) + "&f!"));
                    net.md_5.bungee.api.chat.ClickEvent homeClick = new net.md_5.bungee.api.chat.ClickEvent(
                            net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/animalquest sethome " + subtype);
                    net.md_5.bungee.api.chat.HoverEvent homeHover = new net.md_5.bungee.api.chat.HoverEvent(
                            net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                            new net.md_5.bungee.api.chat.hover.content.Text(Utils.applyFormat("&aClick to confirm!")));
                    for (net.md_5.bungee.api.chat.BaseComponent c : homeMsg) {
                        c.setClickEvent(homeClick);
                        c.setHoverEvent(homeHover);
                    }
                    event.getPlayer().spigot().sendMessage(homeMsg);
                }
            }
        } else if (type.equalsIgnoreCase("MOUNT")) {
            try {
                ca.pandaaa.animalquest.enums.MountType mountType = ca.pandaaa.animalquest.enums.MountType
                        .valueOf(subtype.toUpperCase());
                double price = (mountType.getTier() - 1) * 5000.0;
                addPendingMount(event.getPlayer().getUniqueId(), subtype);
                net.md_5.bungee.api.chat.BaseComponent[] mountMsg = net.md_5.bungee.api.chat.TextComponent
                        .fromLegacyText(
                                Utils.applyFormat(Utils.getAnimalQuestName()
                                        + " &7&l>> &bClick here to purchase mount &6"
                                        + Utils.getSentenceCase(subtype) + " &bfor &3$" + (int) price + "&b."));
                net.md_5.bungee.api.chat.ClickEvent mountClick = new net.md_5.bungee.api.chat.ClickEvent(
                        net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/animalquest buymount " + subtype);
                net.md_5.bungee.api.chat.HoverEvent mountHover = new net.md_5.bungee.api.chat.HoverEvent(
                        net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
                        new net.md_5.bungee.api.chat.hover.content.Text(Utils.applyFormat("&aClick to purchase!")));
                for (net.md_5.bungee.api.chat.BaseComponent c : mountMsg) {
                    c.setClickEvent(mountClick);
                    c.setHoverEvent(mountHover);
                }
                event.getPlayer().spigot().sendMessage(mountMsg);
            } catch (Exception ignored) {
            }
        } else if (type.equalsIgnoreCase("REPAIR")) {
            repairItem(event.getPlayer(), subtype);
        }
    }

    @EventHandler
    public void onNpcDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(npcTypeKey, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    private void repairItem(org.bukkit.entity.Player player, String type) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        if (data == null)
            return;

        if (type.equalsIgnoreCase("sword")) {
            org.bukkit.inventory.ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || item.getType() == org.bukkit.Material.AIR || !item.getType().name().contains("SWORD")) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must hold a sword to repair it!"));
                return;
            }

            if (item.getItemMeta() instanceof org.bukkit.inventory.meta.Damageable) {
                org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) item.getItemMeta();
                if (meta.getDamage() == 0) {
                    player.sendMessage(Utils.applyFormat("&c&l[!] &cThis sword is not damaged!"));
                    return;
                }

                double price = 150.0;
                if (data.getBalance() < price) {
                    player.sendMessage(
                            Utils.applyFormat("&c&l[!] &cYou need " + price + " coins to repair your sword!"));
                    return;
                }

                data.setBalance(data.getBalance() - price);
                meta.setDamage(0);
                item.setItemMeta(meta);
                player.sendMessage(
                        Utils.applyFormat(Utils.getAnimalQuestName()
                                + " &7&l>> &bYour sword has been fully repaired for &3" + price + "&b coins!"));
            }
        } else if (type.equalsIgnoreCase("armor")) {
            org.bukkit.inventory.ItemStack[] armor = player.getInventory().getArmorContents();
            boolean needsRepair = false;
            for (org.bukkit.inventory.ItemStack item : armor) {
                if (item != null && item.getType() != org.bukkit.Material.AIR) {
                    if (item.getItemMeta() instanceof org.bukkit.inventory.meta.Damageable) {
                        org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) item
                                .getItemMeta();
                        if (meta.getDamage() > 0) {
                            needsRepair = true;
                            break;
                        }
                    }
                }
            }

            if (!needsRepair) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cNone of your equipped armor needs repair!"));
                return;
            }

            double price = 250.0;
            if (data.getBalance() < price) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou need " + price + " coins to repair your armor!"));
                return;
            }

            data.setBalance(data.getBalance() - price);
            for (org.bukkit.inventory.ItemStack item : armor) {
                if (item != null && item.getType() != org.bukkit.Material.AIR) {
                    if (item.getItemMeta() instanceof org.bukkit.inventory.meta.Damageable) {
                        org.bukkit.inventory.meta.Damageable meta = (org.bukkit.inventory.meta.Damageable) item
                                .getItemMeta();
                        if (meta.getDamage() > 0) {
                            meta.setDamage(0);
                            item.setItemMeta(meta);
                        }
                    }
                }
            }
            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName()
                    + " &7&l>> &bYour equipped armor has been fully repaired for &3" + price + "&b coins!"));
        }
    }
}
