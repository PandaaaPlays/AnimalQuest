package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaticDischarge extends Spell implements Listener {
    private final Set<UUID> activePlayers = new HashSet<>();

    public StaticDischarge() {
        super("static_discharge", "Static Discharge", 80, 45,
                "Strikes lightning on enemies upon hitting them.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ2NWMxMjE5NThjMDUyMmUzZGNjYjNkMTRkNjg2MTJkNjMxN2NkMzgwYjBlNjQ2YjYxYjc0MjBiOTA0YWYwMiJ9fX0=");
        Bukkit.getPluginManager().registerEvents(this, AnimalQuest.getPlugin());
    }

    @Override
    public void cast(Player player) {
        UUID uuid = player.getUniqueId();
        activePlayers.add(uuid);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 2.0f);
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);

        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            activePlayers.remove(uuid);
            if (player.isOnline()) {
                player.sendMessage(ca.pandaaa.animalquest.utils.Utils.applyFormat("&cStatic Discharge expired."));
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 0.5f);
            }
        }, 20 * 10L);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && activePlayers.contains(attacker.getUniqueId())) {
            if (event.getEntity() instanceof LivingEntity victim && Math.random() < 0.5) {
                victim.getWorld().strikeLightningEffect(victim.getLocation());
                victim.damage(10.0, attacker);
                attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.5f);
            }
        }
    }
}
