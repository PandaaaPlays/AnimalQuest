package ca.pandaaa.animalquest.jobs;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.jobs.manager.JobExperienceManager;

public class JobProgress {

    private static final int MAX_LEVEL = 20;

    private final Job job;
    private int level;
    private double currentExperience;
    private Runnable onChange;
    private Runnable onLevelUp;

    public JobProgress(Job job) {
        this.job = job;
        this.level = 1;
        this.currentExperience = 0.0;
    }

    public JobProgress(Job job, int level, double currentExperience) {
        this.job = job;
        this.level = Math.max(1, Math.min(MAX_LEVEL, level));
        this.currentExperience = Math.max(0, currentExperience);
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public void setOnLevelUp(Runnable onLevelUp) {
        this.onLevelUp = onLevelUp;
    }

    public Job getJob() {
        return job;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return currentExperience;
    }

    public double getGoalExperience() {
        JobExperienceManager manager = getJobExperienceManager();
        if (manager == null) return -1;
        return manager.getGoalForLevel(level);
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public void addExperience(double amount) {
        if (amount <= 0) return;
        this.currentExperience += amount;
        checkLevelUp();
        callOnChange();
    }

    public void setExperience(double experience) {
        this.currentExperience = Math.max(0, experience);
        checkLevelUp();
        callOnChange();
    }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(MAX_LEVEL, level));
        callOnChange();
    }

    private JobExperienceManager getJobExperienceManager() {
        if (AnimalQuest.getPlugin() == null) return null;
        return AnimalQuest.getPlugin().getJobExperienceManager();
    }

    private void checkLevelUp() {
        while (level < MAX_LEVEL) {
            double goal = getGoalExperience();
            if (goal == -1 || currentExperience < goal) break;
            currentExperience -= goal;
            level++;
            callOnLevelUp();
        }
    }

    private void callOnChange() {
        if (onChange != null) onChange.run();
    }

    private void callOnLevelUp() {
        if (onLevelUp != null) onLevelUp.run();
    }
}
