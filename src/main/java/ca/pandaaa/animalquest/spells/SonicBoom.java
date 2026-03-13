package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SonicBoom extends Spell {

    public SonicBoom() {
        super("sonic_boom", "Sonic Boom", 25, 15,
                "Pushes enemies back.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTEzMzIzZjIwZTY0MjFlZjFjMWRjNGU2ZjcwYTdhOGEzODRlMWZjYTUyMjA5ZDY2ZTU1YTliNjg1MmYzMmExZCJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SONIC_BOOM,
                player.getLocation().add(player.getLocation().getDirection().multiply(2)).add(0, 1.5, 0), 10, 0.5, 0.5,
                0.5, 0.1);

        for (Entity entity : player.getNearbyEntities(15, 15, 15)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player) && !(entity instanceof Item)) {
                Vector dir = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                entity.setVelocity(dir.multiply(2.5).setY(0.5));
            }
        }
    }
}
