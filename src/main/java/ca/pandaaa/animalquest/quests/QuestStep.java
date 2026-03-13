package ca.pandaaa.animalquest.quests;

/**
 * Represents a single step in a quest
 */
public class QuestStep {
    private final String description;
    private final QuestObjective objective;

    public QuestStep(String description, QuestObjective objective) {
        this.description = description;
        this.objective = objective;
    }

    public String getDescription() {
        return description;
    }

    public QuestObjective getObjective() {
        return objective;
    }

    public boolean isComplete(int progress) {
        return progress >= objective.getRequiredAmount();
    }

    public String getProgressString(int progress) {
        if (objective.getType() == QuestObjective.ObjectiveType.TALK_TO_NPC)
            return "";
        return progress + "/" + objective.getRequiredAmount();
    }
}
