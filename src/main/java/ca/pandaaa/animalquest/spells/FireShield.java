package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.pandaaa.animalquest.utils.Utils;

public class FireShield extends Spell {

    public FireShield() {
        super("fire_shield", "Fire Shield", 35, 10, "Immunes you and your allies to fire for a short period.");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0));
        player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);

        for (Entity entity : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            if (entity instanceof Player) {
                ((Player) entity).sendMessage(Utils.applyFormat("&d" + player.getName() + " used &5&lFire Shield"));
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
