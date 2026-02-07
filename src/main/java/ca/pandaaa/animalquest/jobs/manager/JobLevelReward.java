package ca.pandaaa.animalquest.jobs.manager;

import ca.pandaaa.animalquest.jobs.Job;
import org.bukkit.entity.Player;

public interface JobLevelReward {

    /**
     * Called when a player levels up in a job.
     *
     * @param player  the player who leveled up
     * @param job     the job that leveled up
     * @param newLevel the new level reached
     */
    void onLevelUp(Player player, Job job, int newLevel);
}
