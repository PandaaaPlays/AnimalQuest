package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpellManager {
    private final PlayerDataManager playerDataManager;
    private final Map<String, Spell> registeredSpells = new HashMap<>();
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public SpellManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void registerSpell(Spell spell) {
        registeredSpells.put(spell.getId().toLowerCase(), spell);
    }

    public void castSpell(Player player, Spell spell) {
        PlayerData data = playerDataManager.get(player.getUniqueId());
        if (data == null)
            return;

        if (isOnCooldown(player, spell)) {
            player.sendMessage(ChatColor.RED + "This spell is on cooldown!");
            return;
        }

        if (data.consumeMana(spell.getManaCost())) {
            spell.cast(player);
            setCooldown(player, spell);
        } else {
            player.sendMessage(ChatColor.RED + "Not enough mana!");
        }
    }

    private boolean isOnCooldown(Player player, Spell spell) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null)
            return false;

        Long lastCast = playerCooldowns.get(spell.getId());
        if (lastCast == null)
            return false;

        return (System.currentTimeMillis() - lastCast) < (spell.getCooldownSeconds() * 1000L);
    }

    private void setCooldown(Player player, Spell spell) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(spell.getId(),
                System.currentTimeMillis());
    }

    public Spell getSpellById(String id) {
        return registeredSpells.get(id.toLowerCase());
    }

    public Map<String, Spell> getRegisteredSpells() {
        return registeredSpells;
    }
}
