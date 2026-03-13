package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GravityPull extends Spell {

    public GravityPull() {
        super("gravity_pull", "Gravity Pull", 60, 25,
                "Pulls all enemies toward you, then pushes them away.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhMzk4N2M1Y2RiMzVhYmE5YWU2ZjJlMjM0ODlhYTk2ZTA4MGU5M2ZhYzQzNWRjNjQwZjczN2I1Y2E0MDFkMyJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 100, 5, 2, 5, 0.01);

        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player) && !(entity instanceof Item)) {
                Vector pullDir = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                entity.setVelocity(pullDir.multiply(1.5));
            }
        }

        org.bukkit.Bukkit.getScheduler().runTaskLater(ca.pandaaa.animalquest.AnimalQuest.getPlugin(), () -> {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.2f);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1, 0), 50, 2, 1, 2,
                    0.1);
            for (Entity entity : player.getNearbyEntities(12, 12, 12)) {
                if (entity instanceof LivingEntity living && !(living instanceof Player) && !(entity instanceof Item)) {
                    Vector pushDir = living.getLocation().toVector().subtract(player.getLocation().toVector())
                            .normalize();
                    living.setVelocity(pushDir.multiply(1.5).setY(0.5));
                    living.damage(6.0, player);
                }
            }
        }, 20L); // 1 second delay (20 ticks)
    }
}
