package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.spells.*;
import ca.pandaaa.animalquest.utils.Utils;
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
        registerSpell(new Charge());
        registerSpell(new CraftsmansAnvil());
        registerSpell(new Cyclone());
        registerSpell(new DragonsStrike());
        registerSpell(new Endurance());
        registerSpell(new Fireball());
        registerSpell(new FireShield());
        registerSpell(new FireSpirit());
        registerSpell(new FlowerShield());
        registerSpell(new HealingSpree());
        registerSpell(new Immortal());
        registerSpell(new LightningSpeed());
        registerSpell(new StoneShield());
        registerSpell(new Strength());
        registerSpell(new AeroGlide());
        registerSpell(new NeptunesBlessing());
        registerSpell(new AbyssalAnchor());
        registerSpell(new GravityPull());
        registerSpell(new SonicBoom());
        registerSpell(new StaticDischarge());
        registerSpell(new VampiricTouch());
        registerSpell(new MeteorRain());
        registerSpell(new PhoenixRebirth());
    }

    public void registerSpell(Spell spell) {
        registeredSpells.put(spell.getId().toLowerCase(), spell);
    }

    public void castSpell(Player player, Spell spell) {
        PlayerData data = playerDataManager.get(player.getUniqueId());
        if (data == null)
            return;

        long remaining = getRemainingCooldown(player, spell);
        if (remaining > 0) {
            if (!(spell.getId().equals("abyssal_anchor") && remaining >= 20)) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cThis spell is on cooldown! (" + remaining + "s)"));
                return;
            }
        }

        if (spell.getId().equals("abyssal_anchor")) {
            spell.cast(player);
            return;
        }

        if (data.consumeMana(spell.getManaCost())) {
            spell.sendActivationMessage(player);
            spell.cast(player);
            setCooldown(player, spell);
        } else {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cNot enough mana!"));
        }
    }

    public long getRemainingCooldown(Player player, Spell spell) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null)
            return 0;

        Long lastCast = playerCooldowns.get(spell.getId());
        if (lastCast == null)
            return 0;

        long timeSinceCast = System.currentTimeMillis() - lastCast;
        long cooldownMillis = spell.getCooldownSeconds() * 1000L;

        if (timeSinceCast >= cooldownMillis)
            return 0;

        return (cooldownMillis - timeSinceCast) / 1000L + 1;
    }

    public void setCooldown(Player player, Spell spell) {
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
