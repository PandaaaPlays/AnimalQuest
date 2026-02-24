package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.events.PlayerAptitudeChangeEvent;
import ca.pandaaa.animalquest.events.PlayerExperienceChangeEvent;
import ca.pandaaa.animalquest.events.PlayerManaChangeEvent;
import ca.pandaaa.animalquest.managers.PlayerDataManager;
import ca.pandaaa.animalquest.managers.ScoreboardManager;
import ca.pandaaa.animalquest.player.Aptitudes;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.CustomUnbreaking;
import ca.pandaaa.animalquest.utils.Formats;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerGameplayListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final ScoreboardManager scoreboardManager;

    public PlayerGameplayListener(PlayerDataManager playerDataManager, ScoreboardManager scoreboard) {
        this.playerDataManager = playerDataManager;
        this.scoreboardManager = scoreboard;
    }

    @EventHandler
    public void onExpBottleThrowEvent(ExpBottleEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            PlayerData data = playerDataManager.get(player.getUniqueId());
            if (data != null) {
                data.getMana().addMana(40);
                event.setExperience(0);
            }
        }
    }

    @EventHandler
    public void playerExperiencePickupEvent(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void playerSneakOnManaBlockEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Material blockUnderPlayer = player.getLocation().add(0, -1, 0).getBlock().getType();
        if (!blockUnderPlayer.equals(Material.LAPIS_BLOCK))
            return;

        if (!player.isSneaking()) {
            PlayerData data = playerDataManager.get(player.getUniqueId());
            if (data != null) {
                data.getMana().addMana(2);
            }
        }
    }

    @EventHandler
    public void onItemDamaged(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        if (item.containsEnchantment(Enchantment.UNBREAKING)) {
            event.setCancelled(true);
            new CustomUnbreaking().damageItem(item);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            double eventBaseDamage = event.getDamage();
            double strengthFromAptitude = (double) playerDataManager.get(player.getUniqueId())
                    .getAptitudes().getStrength() * Aptitudes.STRENGTH_MULTIPLIER;

            // Base damage * (1 + (0.04 * level))
            event.setDamage(eventBaseDamage * (1 + strengthFromAptitude));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.setKeepInventory(true);
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity();
            PlayerData playerData = playerDataManager.get(player.getUniqueId());
            Player killer = event.getEntity().getKiller();
            PlayerData killerData = playerDataManager.get(killer.getUniqueId());
            event.setDeathMessage(Utils.applyFormat(
                    Utils.getAnimalQuestName() + " &7&l>> &3" + player.getName() + "&b was killed by &3"
                            + killer.getName()));

            if (playerData.getBalance() >= 5000) {
                Random random = new Random();
                int randomInt = random.nextInt(2000) + 3000;
                playerData.setBalance(playerData.getBalance() - randomInt);
                player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &3" + killer.getName()
                        + "&c stole &3$" + Formats.formatMoney(randomInt) + "&b from you."));
                killerData.setBalance(killerData.getBalance() + randomInt);
                killer.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bYou &astole &3$"
                        + Formats.formatMoney(randomInt) + "&b from &3" + player.getName() + "&b."));
            }
        }
    }

    @EventHandler
    public void onMountExit(EntityDismountEvent event) {
        if (event.getDismounted().getType() == EntityType.HORSE
                || event.getDismounted().getType() == EntityType.NAUTILUS
                || event.getDismounted().getType() == EntityType.ZOMBIE_NAUTILUS) {
            event.getDismounted().remove();
        }
    }

    @EventHandler
    public void onManaChange(PlayerManaChangeEvent event) {
        PlayerData data = playerDataManager.get(event.getPlayer().getUniqueId());
        if (data != null) {
            data.updateManaDisplay();
        }
    }

    @EventHandler
    public void onExperienceChange(PlayerExperienceChangeEvent event) {
        PlayerData data = playerDataManager.get(event.getPlayer().getUniqueId());
        if (data != null) {
            data.updateScoreboard(event.getPlayer());
            scoreboardManager.updateTablist(event.getPlayer());
        }
    }

    @EventHandler
    public void onAptitudeChange(PlayerAptitudeChangeEvent event) {
        PlayerData data = playerDataManager.get(event.getPlayer().getUniqueId());
        if (data != null && event.getAptitude() == "Mana") {
            data.updateManaDisplay();
        }
    }
}
