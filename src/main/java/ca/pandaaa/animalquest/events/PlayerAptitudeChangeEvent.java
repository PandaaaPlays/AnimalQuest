package ca.pandaaa.animalquest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAptitudeChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String aptitude;
    private final int strength;
    private final int health;
    private final int mana;

    public PlayerAptitudeChangeEvent(Player player, String aptitude, int strength, int health, int mana) {
        this.player = player;
        this.aptitude = aptitude;
        this.strength = strength;
        this.health = health;
        this.mana = mana;
    }

    public Player getPlayer() {
        return player;
    }

    public String getAptitude() {
        return aptitude;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
