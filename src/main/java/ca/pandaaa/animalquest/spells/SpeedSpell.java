package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedSpell extends Spell {
    public SpeedSpell() {
        super("speed", "§bSpeed Spell", 15.0, 10);
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1.5f);
    }
}
