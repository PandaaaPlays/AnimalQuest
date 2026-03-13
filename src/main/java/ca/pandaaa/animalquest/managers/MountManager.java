package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.enums.MountType;
import ca.pandaaa.animalquest.player.Mount;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractNautilus;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MountManager {
    private final PlayerDataManager playerDataManager;
    private final Map<UUID, Long> groundCooldown = new HashMap<>();
    private final Map<UUID, Long> waterCooldown = new HashMap<>();

    public MountManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void spawnMount(Player player) {
        if (isObstructed(player)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cThe area surrounding you is too obstructed."));
            return;
        }

        Location location = player.getLocation();
        Mount mounts = playerDataManager.get(player.getUniqueId()).getMounts();
        boolean inWater = location.getBlock().getType().equals(Material.WATER);
        MountType type = inWater ? mounts.getWaterMount() : mounts.getGroundMount();

        long remaining = getRemainingCooldown(player, type);
        if (remaining > 0) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cMount on cooldown! (" + remaining + "s)"));
            return;
        }

        LivingEntity mount = (LivingEntity) location.getWorld().spawnEntity(location, type.getType());

        if (mount.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
            mount.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        }

        if (mount instanceof Horse horse) {
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            horse.setTamed(true);
            horse.setOwner(player);

            // Apply Skin (Color and Style)
            if (type.getColor() != null)
                horse.setColor(type.getColor());
            if (type.getStyle() != null)
                horse.setStyle(type.getStyle());

            if (horse.getAttribute(Attribute.JUMP_STRENGTH) != null) {
                horse.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(type.getJumpStrength());
            }
        }

        if (mount instanceof AbstractNautilus nautilus) {
            nautilus.getEquipment().setItem(EquipmentSlot.SADDLE, new ItemStack(Material.SADDLE));
            nautilus.setTamed(true);
            nautilus.setOwner(player);
        }

        mount.addPassenger(player);
        if (inWater) {
            waterCooldown.put(player.getUniqueId(), System.currentTimeMillis());
        } else {
            groundCooldown.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    private boolean isObstructed(Player player) {
        boolean isObstructed = false;
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = 0; y < 2; y++) {
                    Location playerLocation = player.getLocation();
                    Location location = new Location(player.getWorld(), playerLocation.getX() + x,
                            playerLocation.getY() + y, playerLocation.getZ() + z);
                    if (!location.getBlock().isPassable()) {
                        isObstructed = true;
                    }
                }
            }
        }
        return isObstructed;
    }

    public long getRemainingCooldown(Player player, MountType type) {
        Long playerCooldown = type.isInWater() ? waterCooldown.get(player.getUniqueId())
                : groundCooldown.get(player.getUniqueId());
        if (playerCooldown == null)
            return 0;
        long timeSinceCast = System.currentTimeMillis() - playerCooldown;
        long cooldownMillis = type.getCooldown() * 1000L;

        if (timeSinceCast >= cooldownMillis)
            return 0;

        return (cooldownMillis - timeSinceCast) / 1000L + 1;
    }
}
