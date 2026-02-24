package ca.pandaaa.animalquest.quests;

/**
 * Represents the objective of a quest step
 */
public class QuestObjective {
    private final ObjectiveType type;
    private final int requiredAmount;
    private final String target; // Can be entity type, material, location, etc.

    public QuestObjective(ObjectiveType type, int requiredAmount, String target) {
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.target = target;
    }

    public ObjectiveType getType() {
        return type;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public String getTarget() {
        return target;
    }

    public enum ObjectiveType {
        KILL_ENTITY, // Kill X entities of type
        COLLECT_ITEM, // Collect X items
        BREAK_BLOCK, // Break X blocks
        PLACE_BLOCK, // Place X blocks
        REACH_LOCATION, // Reach a specific location
        TALK_TO_NPC, // Talk to an NPC (future feature)
        CRAFT_ITEM, // Craft X items
        GAIN_EXPERIENCE, // Gain X experience points
        REACH_LEVEL, // Reach level X
        COMPLETE_JOB // Complete job task
    }
}
