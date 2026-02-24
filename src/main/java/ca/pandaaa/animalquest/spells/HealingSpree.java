package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.pandaaa.animalquest.utils.Utils;

public class HealingSpree extends Spell {

    public HealingSpree() {
        super("healing_spree", "Healing Spree", 35, 20, "Heals you & your allies!",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU2M2JhMDllNzlkYWJmYzYwZWJjY2M2ZTU4ZTM5ZmI1MWFlZjIxMDZlMWNlYTBmZjNiMjYxZGU4OTdjYjZiMiJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 420, 1));
        player.getLocation().getWorld().spawnParticle(Particle.HEART, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);

        for (Entity entity : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
            if (entity instanceof Player) {
                ((Player) entity).sendMessage(Utils.applyFormat("&b" + player.getName() + " used &5&lHealing Spree"));
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 220, 1));
                entity.getLocation().getWorld().spawnParticle(Particle.HEART, entity.getLocation(), 25, 1.0D, 1.0D,
                        1.0D);
            }
        }
    }
}
