package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Immortal extends Spell {

    public Immortal() {
        super("immortal", "Immortal", 200, 60, "Increases your health temporarily!");
    }

    @Override
    public void cast(Player player) {
        player.getLocation().getWorld().spawnParticle(Particle.HEART, player.getLocation(), 30, 1.0D, 1.0D, 1.0D);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 12020, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 12020, 4));
    }
}
