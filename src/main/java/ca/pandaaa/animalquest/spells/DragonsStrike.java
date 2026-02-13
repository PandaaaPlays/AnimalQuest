package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DragonsStrike extends Spell implements Listener {

    private final Set<UUID> jumpingPlayers = new HashSet<>();

    public DragonsStrike() {
        super("dragons_strike", "Dragon's Strike", 75, 20, "Creates an impact strong as the Dragon's strike.",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJlODcwODlmOTMyOWI1NGM5YTU5NjU2MjUzNTQxMDdjN2Y5NmIzMDU0ZjFkZWY4Y2VlYTJiOTBjZTZmOGQifX19");
        Bukkit.getPluginManager().registerEvents(this, AnimalQuest.getPlugin());
    }

    @Override
    public void cast(Player player) {
        player.teleport(player.getLocation().add(0.0D, 0.05D, 0.0D));
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            player.setVelocity(player.getLocation().getDirection().multiply(0.5D).setY(1.5D));
        }, 2L);
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            jumpingPlayers.add(player.getUniqueId());
        }, 15L);
        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            player.setVelocity(player.getLocation().getDirection().multiply(0.1D).setY(-2.0D));
        }, 15L);
        player.getLocation().getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 25, 1.0D, 1.0D, 1.0D);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (jumpingPlayers.contains(player.getUniqueId()) && player.isOnGround()) {
            jumpingPlayers.remove(player.getUniqueId());
            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 15, 2.0D, 1.0D, 2.0D);
            player.getWorld().createExplosion(player.getLocation(), -1.0F);

            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 520, 4));

            for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                Vector vec = new Vector(0, 1.1, 0);
                if (entity != player) {
                    entity.teleport(entity.getLocation().add(0.0D, 0.05D, 0.0D)); // Unstuck
                    Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
                        entity.setVelocity(vec);
                    }, 2L);

                    if (entity instanceof Damageable) {
                        ((Damageable) entity).damage(50.0D, player);
                    }
                }
            }
        }
    }
}
