package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.AbilityType;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityManager implements Listener {
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public AbilityManager(AnimalQuest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR)
            return;

        AbilityType ability = getAbilityFromItem(item);
        if (ability == null)
            return;

        if (isCooldown(player, ability)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cAbility is on cooldown!"));
            return;
        }

        switch (ability) {
            case DASH:
                player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(0.2));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
                break;
            case LEAP:
                player.setVelocity(new Vector(0, 1.2, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
                break;
            default:
                break;
        }

        setCooldown(player, ability, 3000); // 3 seconds default
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        AbilityType ability = getAbilityFromItem(item);
        if (ability == null)
            return;

        switch (ability) {
            case LIGHTNING_STRIKE:
                event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
                event.setDamage(event.getDamage() + 5);
                break;
            case HEALING_TOUCH:
                player.setHealth(Math.min(player.getHealth() + 2,
                    player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                break;
            default:
                break;
        }
    }

    private AbilityType getAbilityFromItem(ItemStack item) {
        if (!item.hasItemMeta())
            return null;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore())
            return null;

        for (String line : meta.getLore()) {
            for (AbilityType type : AbilityType.values()) {
                if (line.contains(Utils.applyFormat(type.getDisplayName()))) {
                    return type;
                }
            }
        }
        return null;
    }

    private boolean isCooldown(Player player, AbilityType type) {
        return cooldowns.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis();
    }

    private void setCooldown(Player player, AbilityType type, long ms) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + ms);
    }
}
