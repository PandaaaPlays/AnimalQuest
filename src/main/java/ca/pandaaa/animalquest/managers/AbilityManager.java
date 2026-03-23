package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.AbilityType;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AbilityManager implements Listener {
    private final Map<UUID, Map<AbilityType, Long>> cooldowns = new HashMap<>();

    public AbilityManager(AnimalQuest plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getHand() == EquipmentSlot.OFF_HAND)
            return;

        Player player = event.getPlayer();

        // Check hand and armor for right-click abilities
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.getType() != Material.AIR) {
                handleActiveAbility(player, item);
            }
        }
        handleActiveAbility(player, player.getInventory().getItemInMainHand());
    }

    private void handleActiveAbility(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return;
        AbilityType ability = getAbilityFromItem(item);
        if (ability == null)
            return;

        if (isCooldown(player, ability))
            return;

        switch (ability) {
            case DASH:
                player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(0.2));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
                break;
            case FIRE_AURA:
                player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                    if (entity instanceof LivingEntity living && !entity.equals(player)) {
                        living.setFireTicks(100);
                        living.damage(2, player);
                    }
                });
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
                break;
            case BLUE_FLAME_STRIKE:
                Location center = player.getLocation();

                // 1. Soul Nova Burst (Omni-directional explosion)
                for (int i = 0; i < 200; i++) {
                    Vector v = Vector.getRandom().subtract(new Vector(0.5, 0.5, 0.5)).normalize()
                            .multiply(Math.random() * 4.5);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, center.clone().add(v).add(0, 1.2, 0), 1,
                            0, 0, 0, 0.02);
                }
                player.getWorld().spawnParticle(Particle.SOUL, center.clone().add(0, 1.2, 0), 50, 0.5, 0.5, 0.5, 0.1);

                // 2. Rising Soul Helix
                for (double h = 0; h < 8; h += 0.2) {
                    double a = h * 1.5;
                    double x1 = Math.cos(a) * (1.5 + h / 4);
                    double z1 = Math.sin(a) * (1.5 + h / 4);
                    player.getWorld().spawnParticle(Particle.SOUL, center.clone().add(x1, h, z1), 3, 0.05, 0.05, 0.05,
                            0.01);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, center.clone().add(-x1, h, -z1), 3, 0.05,
                            0.05, 0.05, 0.01);
                }

                // 3. Shockwave Expanding Rings
                for (int radius = 1; radius <= 6; radius++) {
                    final int r = radius;
                    Bukkit.getScheduler().runTaskLater(AnimalQuest.getPlugin(), () -> {
                        for (int i = 0; i < 360; i += 12) {
                            double angle = Math.toRadians(i);
                            double x = Math.cos(angle) * r;
                            double z = Math.sin(angle) * r;
                            player.getWorld().spawnParticle(Particle.SCULK_SOUL, center.clone().add(x, 0.1, z), 1, 0,
                                    0.1, 0, 0.01);
                        }
                    }, radius);
                }

                // 4. Effects, Massive Damage & Knockback
                player.getWorld().getNearbyEntities(center, 6.0, 4.0, 6.0).forEach(entity -> {
                    if (entity instanceof LivingEntity living && !entity.equals(player)) {
                        living.setFireTicks(240); // 12 seconds
                        living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 160, 2));
                        living.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 140, 3));
                        living.damage(14.0, player);

                        // Impact knockback
                        Vector kb = living.getLocation().toVector().subtract(center.toVector()).normalize()
                                .multiply(2.0).setY(0.7);
                        living.setVelocity(kb);
                    }
                });

                // 5. Sound Layering
                player.playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 2.0f, 0.6f);
                player.playSound(center, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.5f, 0.7f);
                player.playSound(center, Sound.BLOCK_SOUL_SAND_BREAK, 1.5f, 0.5f);

                setCooldown(player, ability, 20000); // 20s cooldown
                return;
            default:
                break;
        }

        setCooldown(player, ability, 4000);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        Player player = (Player) event.getDamager();

        // Check hand for on-hit abilities
        handleHitAbility(event, player, player.getInventory().getItemInMainHand());
    }

    private void handleHitAbility(EntityDamageByEntityEvent event, Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return;
        AbilityType ability = getAbilityFromItem(item);
        if (ability == null)
            return;

        switch (ability) {
            case LIGHTNING_STRIKE:
                event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
                event.setDamage(event.getDamage() + 5);
                break;
            case LIFESTEAL:
                double healAmount = event.getFinalDamage() * 0.05;
                player.setHealth(Math.min(player.getHealth() + healAmount,
                        player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue()));
                player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_BITE, 0.5f, 1.5f);
                break;
            case EXPLOSIVE_HIT:
                if (new Random().nextInt(100) < 15) { // 15% chance
                    event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 2.0f, false, false);
                    event.setDamage(event.getDamage() + 10);
                }
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
        Map<AbilityType, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null)
            return false;
        long time = playerCooldowns.getOrDefault(type, 0L);
        if (time > System.currentTimeMillis()) {
            long remaining = (time - System.currentTimeMillis()) / 1000;
            player.sendMessage(Utils.applyFormat("&c&l[!] &cAbility on cooldown for " + remaining + "s."));
            return true;
        }
        return false;
    }

    private void setCooldown(Player player, AbilityType type, long ms) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(type,
                System.currentTimeMillis() + ms);
    }
}
