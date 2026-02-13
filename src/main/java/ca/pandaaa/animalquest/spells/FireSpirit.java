package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireSpirit extends Spell {

    public FireSpirit() {
        super("fire_spirit", "Fire Spirit", 65, 5, "Makes your tools way better.",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWRlMDk1MzMyNzIwMjE1Y2E5Yjg1ZTdlYWNkMWQwOTJiMTY5N2ZhZDM0ZDY5NmFkZDk0ZDNiNzA5NzY3MDJjIn19fQ==");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 6000, 3));
        player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 40, 1.0D, 1.0D, 1.0D);
    }
}
