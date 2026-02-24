package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cyclone extends Spell {

    public Cyclone() {
        super("cyclone", "Cyclone", 100, 30, "Propel your ennemies in the air!",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNlZDM2ODgyMWI0NjZkMzliZGY1N2U3OGZjMWRkZjc1ZWM5NjZjMTY5NTVlMDJlNTk0OGJlN2FkZmQxZmU1NSJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        cycloneTasks(player);
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            cycloneTasks(player);
        }, 50L);
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            cycloneTasks(player);
        }, 100L);
        player.getLocation().getWorld().spawnParticle(Particle.WITCH, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);
    }

    private void cycloneTasks(Player player) {
        for (Entity entity : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            Vector vec = new Vector(0, 1.7D, 0);
            if (!(entity instanceof Player) && !(entity instanceof Item)) {
                if (entity instanceof Damageable) {
                    ((Damageable) entity).damage(0.5D, player);
                }
                entity.setVelocity(vec);
            }
        }
    }
}
