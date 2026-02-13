package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Immortal extends Spell {

    public Immortal() {
        super("immortal", "Immortal", 200, 60, "Increases your health temporarily!",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNWQ2NTZjNjM1Mjc4MjJjMjE3ZjQyNjdkYTBiNzUyNmU2NTQyNTRiNDFlNDA3N2VhNjc3YmM3Nzg2M2M1YiJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.getLocation().getWorld().spawnParticle(Particle.HEART, player.getLocation(), 30, 1.0D, 1.0D, 1.0D);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 12020, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 12020, 4));
    }
}
