package ca.pandaaa.animalquest.player.experience;

import ca.pandaaa.animalquest.AnimalQuest;

public class Experience {

    private int level;
    private double currentExperience;
    private Runnable onChange;

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public Experience() {
        this.level = 1;
        this.currentExperience = 0.0;
    }

    public Experience(int level, double currentExperience) {
        this.level = level;
        this.currentExperience = currentExperience;
    }

    public double getExperience() {
        return currentExperience;
    }

    public void setExperience(double experience) {
        this.currentExperience = Math.max(0, experience);
        checkLevelUp();
        callOnChange();
    }

    public void addExperience(double amount) {
        if (amount <= 0)
            return;
        this.currentExperience += amount;
        checkLevelUp();
        callOnChange();
    }

    public void removeExperience(double amount) {
        if (amount <= 0)
            return;
        this.currentExperience = Math.max(0, this.currentExperience - amount);
        callOnChange();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
        checkLevelUp();
        callOnChange();
    }

    public void addLevel(int amount) {
        if (amount <= 0)
            return;
        this.level += amount;
        callOnChange();
    }

    public void removeLevel(int amount) {
        if (amount <= 0)
            return;
        this.level = Math.max(1, this.level - amount);
        callOnChange();
    }

    public String getLevelColor() {
        if (level >= 0 && level < 10) {
            return "&7";
        } else if (level >= 10 && level < 20) {
            return "&f";
        } else if (level >= 20 && level < 30) {
            return "&e";
        } else if (level >= 30 && level < 40) {
            return "&6";
        } else if (level >= 40 && level < 50) {
            return "&b";
        } else if (level >= 50 && level < 60) {
            return "&8";
        } else if (level >= 60 && level < 70) {
            return "&b";
        } else if (level >= 70 && level < 80) {
            return "&3";
        } else if (level >= 80 && level < 90) {
            return "&c";
        } else if (level >= 90 && level < 100) {
            return "&4";
        } else {
            return "&5";
        }
    }

    public double getGoalExperience() {
        return AnimalQuest.getPlugin().getExperienceManager().getGoalForLevel(level);
    }

    private void checkLevelUp() {
        while (getGoalExperience() != -1 && currentExperience >= getGoalExperience()) {
            currentExperience -= getGoalExperience();
            level++;
        }
    }

    private void callOnChange() {
        if (onChange != null)
            onChange.run();
    }
}
