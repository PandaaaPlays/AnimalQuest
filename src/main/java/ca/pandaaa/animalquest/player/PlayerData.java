package ca.pandaaa.animalquest.player;

import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.player.jobs.JobProgress;
import ca.pandaaa.animalquest.player.jobs.Jobs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import ca.pandaaa.animalquest.AnimalQuest;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import java.util.UUID;

public class PlayerData implements ConfigurationSerializable {
    private final UUID uuid;
    private final Experience experience;
    private final Mana mana;
    private final Aptitudes aptitudes;
    private final Jobs jobs;
    private final Map<String, Location> homes;
    private int balance;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.experience = new Experience();
        this.mana = new Mana();
        this.aptitudes = new Aptitudes();
        this.jobs = new Jobs();
        this.homes = new HashMap<>();
        this.balance = 0;
        setupListeners();
    }

    public PlayerData(Map<String, Object> map) {
        this.uuid = UUID.fromString((String) map.get("uuid"));

        int level = (int) map.getOrDefault("level", 1);
        double exp = (double) map.getOrDefault("experience", 0.0);
        this.experience = new Experience(level, exp);

        double curMana = (double) map.getOrDefault("mana", 50.0);
        this.mana = new Mana(curMana);

        int str = (int) map.getOrDefault("aptitude_strength", 0);
        int vit = (int) map.getOrDefault("aptitude_health", 0);
        int intl = (int) map.getOrDefault("aptitude_mana", 0);
        this.aptitudes = new Aptitudes(str, vit, intl);

        Object jobsData = map.get("jobs");
        Jobs jobsResult;
        if (jobsData instanceof ConfigurationSection section) {
            jobsResult = new Jobs(section.getValues(false));
        } else {
            jobsResult = new Jobs();
        }
        this.jobs = jobsResult;

        this.balance = (int) map.getOrDefault("balance", 0);
        this.homes = new HashMap<>();
        if (map.containsKey("homes")) {
            ConfigurationSection homeSection = (ConfigurationSection) map.get("homes");
            for (String homeName : homeSection.getKeys(false)) {
                homes.put(homeName, (Location) homeSection.get(homeName));
            }
        }
        setupListeners();
    }

    private void setupListeners() {
        experience.setOnChange(() -> {
            updateScoreboard(org.bukkit.Bukkit.getPlayer(uuid));
            ca.pandaaa.animalquest.AnimalQuest.getPlugin().getScoreboardManager()
                    .updateTablist(org.bukkit.Bukkit.getPlayer(uuid));
        });
        mana.setOnChange(() -> updateManaDisplay(org.bukkit.Bukkit.getPlayer(uuid)));

        var jobsManager = AnimalQuest.getPlugin().getJobsManager();
        if (jobsManager != null) {
            for (Job job : Job.values()) {
                JobProgress progress = jobs.getJob(job);
                progress.setOnLevelUp(() -> {
                    org.bukkit.entity.Player p = org.bukkit.Bukkit.getPlayer(uuid);
                    if (p != null && p.isOnline()) {
                        // jobsManager.onLevelUp(p, job, progress.getLevel());
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        updateScoreboard(org.bukkit.Bukkit.getPlayer(uuid));
    }

    public Map<String, Location> getHomes() {
        return homes;
    }

    public void setHome(String name, Location loc) {
        homes.put(name.toLowerCase(), loc);
    }

    public void removeHome(String name) {
        homes.remove(name.toLowerCase());
    }

    public boolean consumeMana(double amount) {
        return mana.consumeMana(amount);
    }

    public void applyAptitudes(org.bukkit.entity.Player player) {
        // Health: 20 baseline + 2 per point
        double maxHealth = 20.0 + (aptitudes.getHealth() * 2);
        org.bukkit.attribute.AttributeInstance healthAttr = player
                .getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(maxHealth);
        }

        // Mana: 20 baseline + 10 per point
        double maxMana = 50.0 + (aptitudes.getMana() * 10);
        this.mana.setMaximumMana(maxMana);
        updateManaDisplay(player);
    }

    public void updateManaDisplay(org.bukkit.entity.Player player) {
        if (player == null || !player.isOnline())
            return;
        float progress = (float) (mana.getCurrentMana() / mana.getMaximumMana());
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
        map.put("balance", balance);
        map.put("homes", homes);
        return map;
    }
}
