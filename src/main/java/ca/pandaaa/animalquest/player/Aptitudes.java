package ca.pandaaa.animalquest.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.pandaaa.animalquest.events.PlayerAptitudeChangeEvent;

public class Aptitudes {
    private int strength;
    public final static double STRENGTH_MULTIPLIER = 4D / 100D;
    private int health;
    public final static double HEALTH_MULTIPLIER = 2D / 3D;
    private int mana;
    public final static int MANA_MULTIPLIER = 8;
    private final UUID uuid;

    public Aptitudes(UUID uuid) {
        this.uuid = uuid;
        this.strength = 0;
        this.health = 0;
        this.mana = 0;
    }

    public Aptitudes(int strength, int health, int mana, UUID uuid) {
        this.strength = strength;
        this.health = health;
        this.mana = mana;
        this.uuid = uuid;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public void setStrength(int strength) {
        this.strength = strength;
        fireEvent("Strength");
    }

    public void setHealth(int health) {
        this.health = health;
        fireEvent("Health");
    }

    public void setMana(int mana) {
        this.mana = mana;
        fireEvent("Mana");
    }

    private void fireEvent(String aptitude) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Bukkit.getPluginManager().callEvent(
                    new PlayerAptitudeChangeEvent(player, aptitude, strength, health, mana));
        }
    }

    public int getTotalPointsUsed() {
        return strength + health + mana;
    }

}
