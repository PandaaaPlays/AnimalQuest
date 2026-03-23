package ca.pandaaa.animalquest.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.pandaaa.animalquest.events.PlayerManaChangeEvent;

public class Mana {
    private int currentMana;
    private final Aptitudes aptitudes;
    private final UUID uuid;
    private int bonusMaxMana = 0;

    public Mana(Aptitudes aptitudes, UUID uuid) {
        this.aptitudes = aptitudes;
        this.uuid = uuid;
        this.currentMana = 0;
    }

    public Mana(Aptitudes aptitudes, int currentMana, UUID uuid) {
        this.currentMana = currentMana;
        this.aptitudes = aptitudes;
        this.uuid = uuid;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        int base = aptitudes.getMana() * Aptitudes.MANA_MULTIPLIER;
        return Math.max(1, base + bonusMaxMana);
    }

    public void setBonusMaxMana(int bonus) {
        this.bonusMaxMana = bonus;
    }

    public void setCurrentMana(int mana) {
        int oldMana = this.currentMana;
        this.currentMana = Math.max(0, Math.min(mana, getMaxMana()));

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new PlayerManaChangeEvent(player, oldMana, this.currentMana));
        }
    }

    public void addMana(int amount) {
        if (amount <= 0)
            return;
        setCurrentMana(currentMana + amount);
    }

    public boolean consumeMana(int amount) {
        if (currentMana >= amount) {
            setCurrentMana(currentMana - amount);
            return true;
        }
        return false;
    }
}
