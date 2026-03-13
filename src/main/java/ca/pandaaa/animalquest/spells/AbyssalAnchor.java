package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbyssalAnchor extends Spell {
    private final Map<UUID, Location> anchors = new HashMap<>();

    public AbyssalAnchor() {
        super("abyssal_anchor", "Abyssal Anchor", 40, 30,
                "Marks your current location. Cast again within 10 seconds (max 10 blocks) to teleport back.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIyNDYzMWQ3OWQzYWM1Y2U4NzM4Yjc5NDRlYTMzN2YzNTNmYzdmZTUzMmYyMTIzOWU1YWQ3YjhhMzViM2VhNCJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        UUID uuid = player.getUniqueId();
        if (anchors.containsKey(uuid)
                && AnimalQuest.getPlugin().getSpellManager().getRemainingCooldown(player, this) > 20) {
            Location loc = anchors.get(uuid);
            if (loc.getWorld().equals(player.getWorld()) && loc.distance(player.getLocation()) <= 10) {
                if (!AnimalQuest.getPlugin().getPlayerDataManager().get(uuid).consumeMana(getManaCost())) {
                    player.sendMessage(Utils.applyFormat("&c&l[!] &cNot enough mana!"));
                    return;
                }
                sendActivationMessage(player);
                anchors.remove(uuid);
                player.teleport(loc);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 50, 0.5, 1, 0.5,
                        0.1);
                player.sendMessage(Utils.applyFormat("&dTeleported to your Abyssal Anchor!"));
                AnimalQuest.getPlugin().getSpellManager().setCooldown(player, this);
            } else {
                player.sendMessage(
                        Utils.applyFormat("&c&l[!] &cYou are too far from your Abyssal Anchor! (Max 10 blocks)"));
            }
        } else {

            long remaining = AnimalQuest.getPlugin().getSpellManager().getRemainingCooldown(player, this);
            if (remaining > 0) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cThis spell is on cooldown! (" + remaining + "s)"));
                return;
            }
            Location anchorLoc = player.getLocation();
            anchors.put(uuid, anchorLoc);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 0.5f);
            player.getWorld().spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 30, 0.2, 0.5, 0.2,
                    0.05);
            player.sendMessage(Utils.applyFormat("&dAbyssal Anchor set! Cast again (within 10 blocks) to return."));
            AnimalQuest.getPlugin().getSpellManager().setCooldown(player, this);

            Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
                if (anchors.get(uuid) == anchorLoc) {
                    anchors.remove(uuid);
                    if (player.isOnline()) {
                        player.sendMessage(Utils.applyFormat("&c&l[!] &cAbyssal Anchor expired."));
                    }
                }
            }, 20 * 10L);
        }
    }
}
