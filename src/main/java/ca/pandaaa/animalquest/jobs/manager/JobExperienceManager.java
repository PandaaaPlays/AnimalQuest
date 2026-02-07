package ca.pandaaa.animalquest.jobs.manager;

public interface JobExperienceManager {

    /**
     * Returns the experience required to level up from the given level.
     * Returns -1 if the level is invalid or max level is reached.
     */
    double getGoalForLevel(int level);
}
