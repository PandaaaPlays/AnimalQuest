package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength extends Spell {

    public Strength() {
        super("strength", "Strength", 75, 5, "Makes you stronger for a short period.",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFjNTAxM2IyMjdhNDk0ZWMxNmM0ZWFmYWIxZjg0ZGVhNTcyZWZiMmYzYzRiNWMzZmY4ODI1MmYzNTZhMDJiIn19fQ==");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 920, 2));
        player.getLocation().getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation(), 25, 1.0D, 1.0D,
            1.0D);
    }
}
