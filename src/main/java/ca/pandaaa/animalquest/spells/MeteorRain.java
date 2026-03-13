package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MeteorRain extends Spell {

    public MeteorRain() {
        super("meteor_rain", "Meteor Rain", 150, 100,
                "Summons fireballs from the sky.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzExNDdkODlkMjQ5OGY5ZTIxZjM2YWQzZTUzNTZiMjMyN2M4Zjg1NTE4M2QzZTY5ZjRkNjYwZTViYzIxMGFiYiJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        Location center = player.getTargetBlock(null, 30).getLocation();
        if (center == null)
            center = player.getLocation();
        final Location finalCenter = center;

        new BukkitRunnable() {
            int meteorCount = 0;

            @Override
            public void run() {
                if (meteorCount >= 5) {
                    this.cancel();
                    return;
                }

                Location skyLoc = finalCenter.clone().add(
                        Math.random() * 6 - 3,
                        15,
                        Math.random() * 6 - 3);

                Fireball fireball = skyLoc.getWorld().spawn(skyLoc, Fireball.class);
                fireball.setDirection(new Vector(0, -1, 0));
                fireball.setMetadata("spell", new FixedMetadataValue(AnimalQuest.getPlugin(), "fireball"));
                fireball.setYield(2.0f);
                fireball.setIsIncendiary(true);

                fireball.getWorld().playSound(skyLoc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.5f);
                fireball.getWorld().spawnParticle(Particle.FLAME, skyLoc, 50, 0.5, 0.5, 0.5, 0.1);

                meteorCount++;
            }
        }.runTaskTimer(AnimalQuest.getPlugin(), 0L, 10L);
    }
}
