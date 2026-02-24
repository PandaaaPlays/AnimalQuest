package ca.pandaaa.animalquest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerManaChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int oldMana;
    private final int newMana;

    public PlayerManaChangeEvent(Player player, int oldMana, int newMana) {
        this.player = player;
        this.oldMana = oldMana;
        this.newMana = newMana;
    }

    public Player getPlayer() {
        return player;
    }

    public int getOldMana() {
        return oldMana;
    }

    public int getNewMana() {
        return newMana;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
