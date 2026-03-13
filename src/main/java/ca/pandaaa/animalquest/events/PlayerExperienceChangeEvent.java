package ca.pandaaa.animalquest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerExperienceChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int level;
    private final double exp;
    private final double addedExp;

    public PlayerExperienceChangeEvent(Player player, int level, double exp, double addedExp) {
        this.player = player;
        this.level = level;
        this.exp = exp;
        this.addedExp = addedExp;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public double getAddedExp() {
        return addedExp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
