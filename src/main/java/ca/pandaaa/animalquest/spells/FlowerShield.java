package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.pandaaa.animalquest.utils.Utils;

public class FlowerShield extends Spell {

    public FlowerShield() {
        super("flower_shield", "Flower Shield", 35, 10, "Gives you absobrtion hearts to protect you.");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 6020, 2));
        player.getLocation().getWorld().spawnParticle(Particle.FALLING_WATER, player.getLocation(), 25, 1.0D, 1.0D,
                1.0D);
        player.getLocation().getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);

        for (Entity entity : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            if (entity instanceof Player) {
                ((Player) entity).sendMessage(Utils.applyFormat("&d" + player.getName() + " used &5&lFlower Shield"));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 6020, 1));
                entity.getLocation().getWorld().spawnParticle(Particle.FALLING_WATER, entity.getLocation(), 25, 1.0D,
                        1.0D,
                        1.0D);
                entity.getLocation().getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 25, 1.0D, 1.0D,
                        1.0D);
            }
        }
    }
}
