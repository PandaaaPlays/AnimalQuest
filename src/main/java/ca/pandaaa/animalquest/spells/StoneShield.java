package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StoneShield extends Spell {

    public StoneShield() {
        super("stone_shield", "Stone Shield", 30, 20, "Increases your resistance to damages!");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3620, 0));
        player.getLocation().getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 30, 1.0D, 1.0D, 1.0D);
    }
}
