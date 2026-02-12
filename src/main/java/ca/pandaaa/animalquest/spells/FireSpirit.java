package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireSpirit extends Spell {

    public FireSpirit() {
        super("fire_spirit", "Fire Spirit", 65, 5, "Makes your tools way better.");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 6000, 3));
        player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 40, 1.0D, 1.0D, 1.0D);
    }
}
