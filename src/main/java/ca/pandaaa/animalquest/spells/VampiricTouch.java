package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VampiricTouch extends Spell implements Listener {
    private final Set<UUID> activePlayers = new HashSet<>();

    public VampiricTouch() {
        super("vampiric_touch", "Vampiric Touch", 100, 60,
                "Melee damage you deal has a chance to heal you.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFhOWNiZDk3MDYzMTI3Njc1OWQ1ZDc2MjljYWFjYTUwYTI2ZmM3YjJkYjc4NjVlZjQ3MDllNWRkMmM2YjgwMCJ9fX0=");
        Bukkit.getPluginManager().registerEvents(this, AnimalQuest.getPlugin());
    }

    @Override
    public void cast(Player player) {
        UUID uuid = player.getUniqueId();
        activePlayers.add(uuid);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5,
                0.1);

        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            activePlayers.remove(uuid);
        }, 20 * 10L);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && activePlayers.contains(attacker.getUniqueId())) {
            double healAmount = event.getFinalDamage() * 0.2;
            if (healAmount > 0) {
                double newHealth = Math.min(attacker.getAttribute(Attribute.MAX_HEALTH).getValue(),
                        attacker.getHealth() + healAmount);
                attacker.setHealth(newHealth);
                attacker.getWorld().spawnParticle(Particle.HEART, attacker.getLocation().add(0, 1.5, 0), 3, 0.2, 0.2,
                        0.2, 0.1);
                attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_WITCH_DRINK, 0.5f, 1.2f);
            }
        }
    }
}
