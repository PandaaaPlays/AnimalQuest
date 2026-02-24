package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.pandaaa.animalquest.utils.Utils;

public class FireShield extends Spell {

    public FireShield() {
        super("fire_shield", "Fire Shield", 35, 10, "Immunes you and your allies to fire for a short period.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUxYzRmZGZiZWYyOTIzNjQyNDc2NmY3ZjM4MzNjYzA2OGM5MmM0NzJhYjE0ZDk0NmE1N2Y2YzMyZWQ4In19fQ==");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0));
        player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);

        for (Entity entity : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            if (entity instanceof Player) {
                ((Player) entity).sendMessage(Utils.applyFormat("&b" + player.getName() + " used &5&lFire Shield"));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0));
                entity.getLocation().getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 25, 1.0D, 1.0D,
                        1.0D);
                entity.getLocation().getWorld().spawnParticle(Particle.DRIPPING_LAVA, entity.getLocation(), 25, 1.0D,
                        1.0D,
                        1.0D);
            }
        }
    }
}
