package ca.pandaaa.animalquest.jobs.manager;

import ca.pandaaa.animalquest.jobs.Job;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JobLevelRewardManager {

    private final List<JobLevelReward> rewards = new ArrayList<>();

    public void addReward(JobLevelReward reward) {
        rewards.add(reward);
    }

    public void onLevelUp(Player player, Job job, int newLevel) {
        for (JobLevelReward reward : rewards) {
            reward.onLevelUp(player, job, newLevel);
        }
    }
}
