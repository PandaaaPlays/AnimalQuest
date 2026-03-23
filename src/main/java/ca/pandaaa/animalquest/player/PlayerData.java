package ca.pandaaa.animalquest.player;

import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.player.jobs.JobProgress;
import ca.pandaaa.animalquest.player.jobs.Jobs;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import ca.pandaaa.animalquest.AnimalQuest;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData implements ConfigurationSerializable {
    public final static double HEALTH_PER_LEVEL_MULTIPLICATOR = 2D / 5D;
    private final UUID uuid;
    private final Experience experience;
    private final Mana mana;
    private final Aptitudes aptitudes;
    private final Jobs jobs;
    private final Quests quests;
    private final Mount mounts;
    private String home;
    private double balance;
    private boolean vanished;
    private final Statistics statistics;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.experience = new Experience(uuid);
        this.aptitudes = new Aptitudes(uuid);
        this.mana = new Mana(aptitudes, uuid);
        this.jobs = new Jobs();
        this.quests = new Quests();
        this.home = "";
        this.mounts = new Mount();
        this.balance = 0;
        this.vanished = false;
        this.statistics = new Statistics();
        setupListeners();
    }

    public PlayerData(Map<String, Object> map) {
        this.uuid = UUID.fromString((String) map.get("uuid"));

        int level = (int) map.getOrDefault("level", 1);
        double exp = (double) map.getOrDefault("experience", 0.0);
        this.experience = new Experience(level, exp, uuid);

        int curMana = (int) map.getOrDefault("mana", 0);
        int str = (int) map.getOrDefault("aptitude_strength", 0);
        int vit = (int) map.getOrDefault("aptitude_health", 0);
        int intl = (int) map.getOrDefault("aptitude_mana", 0);
        this.aptitudes = new Aptitudes(str, vit, intl, uuid);
        this.mana = new Mana(aptitudes, curMana, uuid);

        Object jobsData = map.get("jobs");
        Jobs jobsResult;
        if (jobsData instanceof ConfigurationSection section) {
            jobsResult = new Jobs(section.getValues(false));
        } else {
            jobsResult = new Jobs();
        }
        this.jobs = jobsResult;

        Object questsData = map.get("quests");
        Quests questsResult;
        if (questsData instanceof ConfigurationSection section) {
            questsResult = new Quests(section.getValues(false));
        } else {
            questsResult = new Quests();
        }
        this.quests = questsResult;

        Object mountData = map.get("mounts");
        Mount mount;
        if (mountData instanceof ConfigurationSection section) {
            mount = new Mount(section.getValues(false));
        } else {
            mount = new Mount();
        }
        this.mounts = mount;

        this.balance = (double) map.getOrDefault("balance", 0);
        this.home = (String) map.getOrDefault("home", "");
        this.vanished = (boolean) map.getOrDefault("vanished", false);

        if (map.get("statistics") instanceof Map statsMap) {
            @SuppressWarnings("unchecked")
            Map<String, Object> castStatsMap = (Map<String, Object>) statsMap;
            this.statistics = new Statistics(castStatsMap);
        } else {
            this.statistics = new Statistics();
        }

        setupListeners();
    }

    private void setupListeners() {
        var jobsManager = AnimalQuest.getPlugin().getJobsManager();
        if (jobsManager != null) {
            for (Job job : Job.values()) {
                JobProgress progress = jobs.getJob(job);
                progress.setOnLevelUp(() -> {
                    org.bukkit.entity.Player p = org.bukkit.Bukkit.getPlayer(uuid);
                    if (p != null && p.isOnline()) {
                        jobsManager.sendJobLevelUpMessage(p, job, progress.getLevel());
                        if (job == Job.EXPLORER) {
                            applyExplorerSpeed();
                            applyExplorerManaBonus();
                        }
                    }
                });
            }
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Experience getExperience() {
        return experience;
    }

    public Mana getMana() {
        return mana;
    }

    public Aptitudes getAptitudes() {
        return aptitudes;
    }

    public Jobs getJobs() {
        return jobs;
    }

    public Quests getQuests() {
        return quests;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        updateScoreboard(org.bukkit.Bukkit.getPlayer(uuid));
    }

    public Location getHome() {
        switch (home.toLowerCase()) {
            case "capital":
                return new Location(Bukkit.getWorld("world"), -42.5, 28, 3.5, 135f, 0f);
        }
        return new Location(Bukkit.getWorld("world"), 695.5, 39, -40.5, 0f, 0f);
    }

    public String getHomeName() {
        return home;
    }

    public void setHome(String name) {
        home = name;
    }

    public Mount getMounts() {
        return mounts;
    }

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public boolean consumeMana(int amount) {
        return mana.consumeMana(amount);
    }

    public void applyHealthAptitude() {
        double healthFromLevel = experience.getLevel() * HEALTH_PER_LEVEL_MULTIPLICATOR;
        double healthFromAptitude = aptitudes.getHealth() * Aptitudes.HEALTH_MULTIPLIER;
        double maxHealth = 20.0 + healthFromAptitude + healthFromLevel;
        AttributeInstance healthAttr = Objects.requireNonNull(Bukkit.getPlayer(uuid))
                .getAttribute(Attribute.MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(maxHealth);
        }
    }

    public void applyExplorerSpeed() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return;
        AttributeInstance speedAttr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        if (speedAttr != null) {
            int explorerLevel = jobs.getExplorer().getLevel();
            // 0.5% increase per level (0.0005), 10% increase at level 20 (0.01)
            double bonus = explorerLevel * 0.0005;
            speedAttr.setBaseValue(0.1 + bonus);
        }
    }

    public void applyExplorerManaBonus() {
        int explorerLevel = jobs.getExplorer().getLevel();
        // 5 mana per level, 100 mana at level 20
        int bonus = explorerLevel * 5;
        mana.setBonusMaxMana(bonus);
        updateManaDisplay();
    }

    public void updateManaDisplay() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline())
            return;
        float maxMana = mana.getMaxMana();
        float progress = (float) (mana.getCurrentMana() / maxMana);
        player.setExp(Math.min(0.999f, progress));
        player.setLevel((int) mana.getCurrentMana());
    }

    public void updateScoreboard(org.bukkit.entity.Player player) {
        if (player == null || !player.isOnline())
            return;
        ca.pandaaa.animalquest.AnimalQuest.getPlugin().getScoreboardManager().updateScoreboard(player, false);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("level", experience.getLevel());
        map.put("experience", experience.getExperience());
        map.put("mana", mana.getCurrentMana());
        map.put("aptitude_strength", aptitudes.getStrength());
        map.put("aptitude_health", aptitudes.getHealth());
        map.put("aptitude_mana", aptitudes.getMana());
        map.put("jobs", jobs.serialize());
        map.put("quests", quests.serialize());
        map.put("balance", balance);
        map.put("home", home);
        map.put("mounts", mounts.serialize());
        map.put("vanished", vanished);
        map.put("statistics", statistics.serialize());
        return map;
    }
}
