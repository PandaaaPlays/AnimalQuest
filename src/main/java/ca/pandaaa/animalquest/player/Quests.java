package ca.pandaaa.animalquest.player;

import ca.pandaaa.animalquest.quests.QuestProgress;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

/**
 * Manages all quests for a player
 */
public class Quests implements ConfigurationSerializable {
    private final Map<String, QuestProgress> activeQuests;
    private final Set<String> completedQuests;
    private String trackedQuestId; // The quest being displayed above hotbar

    public Quests() {
        this.activeQuests = new HashMap<>();
        this.completedQuests = new HashSet<>();
        this.trackedQuestId = null;
    }

    public Quests(Map<String, Object> map) {
        this.activeQuests = new HashMap<>();
        this.completedQuests = new HashSet<>();
        this.trackedQuestId = (String) map.get("trackedQuestId");

        // Load active quests
        if (map.containsKey("activeQuests")) {
            ConfigurationSection activeSection = (ConfigurationSection) map.get("activeQuests");
            for (String questId : activeSection.getKeys(false)) {
                ConfigurationSection questSection = activeSection.getConfigurationSection(questId);
                if (questSection != null) {
                    QuestProgress progress = new QuestProgress(questSection.getValues(false));
                    activeQuests.put(questId, progress);
                }
            }
        }

        // Load completed quests
        if (map.containsKey("completedQuests")) {
            List<?> completed = (List<?>) map.get("completedQuests");
            for (Object questId : completed) {
                completedQuests.add((String) questId);
            }
        }
    }

    public void startQuest(String questId) {
        if (!activeQuests.containsKey(questId) && !completedQuests.contains(questId)) {
            activeQuests.put(questId, new QuestProgress(questId));

            // Auto-track if no quest is currently tracked
            if (trackedQuestId == null) {
                trackedQuestId = questId;
            }
        }
    }

    public QuestProgress getQuestProgress(String questId) {
        return activeQuests.get(questId);
    }

    public void completeQuest(String questId) {
        QuestProgress progress = activeQuests.get(questId);
        if (progress != null) {
            progress.setStatus(QuestProgress.QuestStatus.COMPLETED);
            completedQuests.add(questId);
            activeQuests.remove(questId);

            // Untrack if this was the tracked quest
            if (questId.equals(trackedQuestId)) {
                trackedQuestId = null;
                // Auto-track next active quest if available
                if (!activeQuests.isEmpty()) {
                    trackedQuestId = activeQuests.keySet().iterator().next();
                }
            }
        }
    }

    public void abandonQuest(String questId) {
        activeQuests.remove(questId);
        if (questId.equals(trackedQuestId)) {
            trackedQuestId = null;
        }
    }

    public boolean hasActiveQuest(String questId) {
        return activeQuests.containsKey(questId);
    }

    public boolean hasCompletedQuest(String questId) {
        return completedQuests.contains(questId);
    }

    public Map<String, QuestProgress> getActiveQuests() {
        return new HashMap<>(activeQuests);
    }

    public Set<String> getCompletedQuests() {
        return new HashSet<>(completedQuests);
    }

    public String getTrackedQuestId() {
        return trackedQuestId;
    }

    public void setTrackedQuestId(String questId) {
        if (questId == null || activeQuests.containsKey(questId)) {
            this.trackedQuestId = questId;
        }
    }

    public QuestProgress getTrackedQuest() {
        return trackedQuestId != null ? activeQuests.get(trackedQuestId) : null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        // Serialize active quests
        Map<String, Object> activeMap = new HashMap<>();
        for (Map.Entry<String, QuestProgress> entry : activeQuests.entrySet()) {
            activeMap.put(entry.getKey(), entry.getValue().serialize());
        }
        map.put("activeQuests", activeMap);

        // Serialize completed quests
        map.put("completedQuests", new ArrayList<>(completedQuests));

        // Serialize tracked quest
        if (trackedQuestId != null) {
            map.put("trackedQuestId", trackedQuestId);
        }

        return map;
    }
}
