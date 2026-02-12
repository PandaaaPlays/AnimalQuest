package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Endurance extends Spell {

    public Endurance() {
        super("endurance", "Endurance", 50, 10, "Stops your hunger and makes you a little faster.");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3620, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 0));
        player.getLocation().getWorld().spawnParticle(Particle.HEART, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);
    }
}
