package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cyclone extends Spell {

    public Cyclone() {
        super("cyclone", "Cyclone", 100, 30, "Propel your ennemies in the air!");
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
            if (!(entity instanceof Player)) {
                if (entity instanceof Damageable) {
                    ((Damageable) entity).damage(0.5D, player);
                }
                entity.setVelocity(vec);
            }
        }
    }
}
