package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NeptunesBlessing extends Spell {
    public NeptunesBlessing() {
        super("neptunes_blessing", "Neptune's Blessing", 30, 60,
                "Receive the blessing of the sea. Grants water breathing, night vision, and swiftness in the water.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM1NmFmZDYyMTFhNWU2Zjk2NjYxZDIzZGYzMjVmMDUzYWU2YWFmMTJiZWY5YzI5ZjdkN2UxMmFjOTZjNmNmIn19fQ==");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 45, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 45, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 20 * 30, 0));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.BUBBLE, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.1);
    }
}
