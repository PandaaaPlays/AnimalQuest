package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StoneShield extends Spell {

    public StoneShield() {
        super("stone_shield", "Stone Shield", 30, 20, "Increases your resistance to damages!",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1ZWU5YzY0ZDRiNjYyMTE5ZjVjNTczNTU2MjMyNzQwMDE4NTgyMjM5YjA5OTNlZTZjYTMyNTJmZmVlMWY2NyJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 3620, 0));
        player.getLocation().getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 30, 1.0D, 1.0D, 1.0D);
    }
}
