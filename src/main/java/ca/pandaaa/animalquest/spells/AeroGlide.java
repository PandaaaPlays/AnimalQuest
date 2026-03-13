package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AeroGlide extends Spell {
    public AeroGlide() {
        super("aero_glide", "Aero Glide", 25, 45,
                "Harness the wind to glide through the air. Grants slow falling and a temporary jump boost.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTI5YWQ3NmMyNDU5OTExYzJiYmFjZGNkMGE3YjgyZTA4MzU1NjdlM2U1MTM0YjA1YTZmNWFmNjY5ZGQ4OGI4MyJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 5, 2));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_BREATHE, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.2, 0.5, 0.05);
    }
}
