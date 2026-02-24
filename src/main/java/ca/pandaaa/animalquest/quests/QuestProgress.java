package ca.pandaaa.animalquest.quests;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks a player's progress on a specific quest
 */
public class QuestProgress implements ConfigurationSerializable {
    private final String questId;
    private int currentStep;
    private int currentProgress;
    private QuestStatus status;
    private long startTime;
    private long completionTime;

    public QuestProgress(String questId) {
        this.questId = questId;
        this.currentStep = 0;
        this.currentProgress = 0;
        this.status = QuestStatus.IN_PROGRESS;
        this.startTime = System.currentTimeMillis();
        this.completionTime = 0;
    }

    public QuestProgress(Map<String, Object> map) {
        this.questId = (String) map.get("questId");
        this.currentStep = (int) map.getOrDefault("currentStep", 0);
        this.currentProgress = (int) map.getOrDefault("currentProgress", 0);
        this.status = QuestStatus.valueOf((String) map.getOrDefault("status", "IN_PROGRESS"));
        this.startTime = ((Number) map.getOrDefault("startTime", System.currentTimeMillis())).longValue();
        this.completionTime = ((Number) map.getOrDefault("completionTime", 0L)).longValue();
    }

    public String getQuestId() {
        return questId;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        this.currentProgress = 0; // Reset progress when moving to next step
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public void addProgress(int amount) {
        this.currentProgress += amount;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
        if (status == QuestStatus.COMPLETED) {
            this.completionTime = System.currentTimeMillis();
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public boolean isCompleted() {
        return status == QuestStatus.COMPLETED;
    }

    public boolean isInProgress() {
        return status == QuestStatus.IN_PROGRESS;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("questId", questId);
        map.put("currentStep", currentStep);
        map.put("currentProgress", currentProgress);
        map.put("status", status.name());
        map.put("startTime", startTime);
        map.put("completionTime", completionTime);
        return map;
    }

    public enum QuestStatus {
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }
}
