package ca.pandaaa.animalquest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJobLevelUpEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String jobName;
    private final int newLevel;

    public PlayerJobLevelUpEvent(Player player, String jobName, int newLevel) {
        this.player = player;
        this.jobName = jobName;
        this.newLevel = newLevel;
    }

    public Player getPlayer() {
        return player;
    }

    public String getJobName() {
        return jobName;
    }

    public int getNewLevel() {
        return newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
