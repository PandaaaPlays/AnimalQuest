package ca.pandaaa.animalquest.player;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Statistics implements ConfigurationSerializable {
    private final Map<String, Integer> customMobKills;
    private final Map<String, Long> firstBuyTimes; // Timestamp of first purchase
    private final Map<Integer, Long> levelUpTimes;
    private long firstJoinTime;

    public Statistics() {
        this.customMobKills = new HashMap<>();
        this.firstBuyTimes = new HashMap<>();
        this.levelUpTimes = new HashMap<>();
        this.firstJoinTime = System.currentTimeMillis();
        logLevelUp(1);
    }

    @SuppressWarnings("unchecked")
    public Statistics(Map<String, Object> map) {
        this.customMobKills = (Map<String, Integer>) map.getOrDefault("custom_mob_kills", new HashMap<String, Integer>());
        this.firstBuyTimes = (Map<String, Long>) map.getOrDefault("first_buy_times", new HashMap<String, Long>());
        this.levelUpTimes = map.containsKey("level_up_times") ? (Map<Integer, Long>) map.get("level_up_times") : new HashMap<Integer, Long>();
        this.firstJoinTime = map.containsKey("first_join_time") ? ((Number) map.get("first_join_time")).longValue() : System.currentTimeMillis();
    }

    public void addKill(String mobId) {
        customMobKills.put(mobId, customMobKills.getOrDefault(mobId, 0) + 1);
    }

    public void logPurchase(String itemId) {
        if (!firstBuyTimes.containsKey(itemId)) {
            firstBuyTimes.put(itemId, System.currentTimeMillis());
        }
    }

    public long getTimeBeforePurchase(String itemId) {
        if (!firstBuyTimes.containsKey(itemId)) return -1;
        return firstBuyTimes.get(itemId) - firstJoinTime;
    }

    public int getKills(String mobId) {
        return customMobKills.getOrDefault(mobId, 0);
    }

    public void logLevelUp(int level) {
        if (!levelUpTimes.containsKey(level)) {
            levelUpTimes.put(level, System.currentTimeMillis());
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("custom_mob_kills", customMobKills);
        map.put("first_buy_times", firstBuyTimes);
        map.put("level_up_times", levelUpTimes);
        map.put("first_join_time", firstJoinTime);
        return map;
    }
}
