package ca.pandaaa.animalquest.jobs;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Jobs implements ConfigurationSerializable {

    private final JobProgress lumberjack;
    private final JobProgress miner;
    private final JobProgress alchemist;
    private final JobProgress explorer;

    public Jobs() {
        this.lumberjack = new JobProgress(Job.LUMBERJACK);
        this.miner = new JobProgress(Job.MINER);
        this.alchemist = new JobProgress(Job.ALCHEMIST);
        this.explorer = new JobProgress(Job.EXPLORER);
    }

    public Jobs(Map<String, Object> map) {
        this.lumberjack = loadJobProgress(Job.LUMBERJACK, map, "lumberjack");
        this.miner = loadJobProgress(Job.MINER, map, "miner");
        this.alchemist = loadJobProgress(Job.ALCHEMIST, map, "alchemist");
        this.explorer = loadJobProgress(Job.EXPLORER, map, "explorer");
    }

    private static JobProgress loadJobProgress(Job job, Map<String, Object> map, String prefix) {
        int level = toInt(map.get(prefix + "_level"), 1);
        double exp = toDouble(map.get(prefix + "_experience"), 0.0);
        return new JobProgress(job, level, exp);
    }

    private static int toInt(Object value, int defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Number n) return n.intValue();
        return defaultValue;
    }

    private static double toDouble(Object value, double defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Number n) return n.doubleValue();
        return defaultValue;
    }

    public JobProgress getLumberjack() {
        return lumberjack;
    }

    public JobProgress getMiner() {
        return miner;
    }

    public JobProgress getAlchemist() {
        return alchemist;
    }

    public JobProgress getExplorer() {
        return explorer;
    }

    public JobProgress getJob(Job job) {
        return switch (job) {
            case LUMBERJACK -> lumberjack;
            case MINER -> miner;
            case ALCHEMIST -> alchemist;
            case EXPLORER -> explorer;
        };
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        serializeJob(map, "lumberjack", lumberjack);
        serializeJob(map, "miner", miner);
        serializeJob(map, "alchemist", alchemist);
        serializeJob(map, "explorer", explorer);
        return map;
    }

    private static void serializeJob(Map<String, Object> map, String prefix, JobProgress progress) {
        map.put(prefix + "_level", progress.getLevel());
        map.put(prefix + "_experience", progress.getExperience());
    }
}
