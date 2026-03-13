package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PhoenixRebirth extends Spell implements Listener {
    private final Set<UUID> activePlayers = new HashSet<>();

    public PhoenixRebirth() {
        super("phoenix_rebirth", "Phoenix Rebirth", 200, 600,
                "Fatal damage triggers a fiery explosion and restores half health.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDcyNzYwYjljOGY1ODc5ZDk4NWExY2E3MGVlZjU3MWFkYmY2ODM1M2VkMThiOTUyNDViNjIwYTU2YTUzZWRiIn19fQ==");
        Bukkit.getPluginManager().registerEvents(this, AnimalQuest.getPlugin());
    }

    @Override
    public void cast(Player player) {
        UUID uuid = player.getUniqueId();
        activePlayers.add(uuid);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1, 0), 100, 0.5, 1, 0.5, 0.05);
        player.sendMessage(ca.pandaaa.animalquest.utils.Utils
                .applyFormat("&6You are now blessed with Phoenix Rebirth for 10 minutes!"));

        Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
            if (activePlayers.remove(uuid) && player.isOnline()) {
                player.sendMessage(ca.pandaaa.animalquest.utils.Utils.applyFormat("&cPhoenix Rebirth has expired."));
            }
        }, 20 * 60 * 10L);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && activePlayers.contains(player.getUniqueId())) {
            if (player.getHealth() - event.getFinalDamage() <= 0) {
                event.setCancelled(true);
                activePlayers.remove(player.getUniqueId());

                player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue() / 2);
                player.getWorld().createExplosion(player.getLocation(), 4.0f, true, false);
                player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 100, 2, 2, 2, 0.1);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.5f);

                player.sendMessage(ca.pandaaa.animalquest.utils.Utils
                        .applyFormat("&6&lPHOENIX REBIRTH! &eYou have been reborn in fire!"));
            }
        }
    }
}
