package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Charge extends Spell {

    public Charge() {
        super("charge", "Charge", 90, 15, "Considerably increases your strength for really limited time.");
    }

    @Override
    public void cast(final Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 41, 4));
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), new Runnable() {
            public void run() {
                player.getLocation().getWorld().spawnParticle(Particle.CRIT, player.getLocation(), 25, 1.0D, 1.0D,
                        1.0D);
                player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 25, 1.0D, 1.0D,
                        1.0D);
                player.getLocation().getWorld().spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 25, 1.0D,
                        1.0D,
                        1.0D);
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10, 255));
                player.sendMessage(Utils.applyFormat("&cIncreased &4&lStrength"));
            }
        }, 40L);
    }
}
