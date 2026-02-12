package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LightningSpeed extends Spell {

    public LightningSpeed() {
        super("lightning_speed", "Lightning Speed", 80, 10, "Makes you fast as the lightning!");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 81, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 81, 5));
        player.getLocation().getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);
        player.getLocation().getWorld().spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation(), 25, 1.0D, 1.0D,
                1.0D);
    }
}
