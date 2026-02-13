package ca.pandaaa.animalquest;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Guild implements ConfigurationSerializable {
    private String name;
    private String tag;
    private UUID owner;
    private Set<UUID> members;
    private Set<UUID> officers;
    private double experience;
    private int level;

    public Guild(String name, String tag, UUID owner) {
        this.name = name;
        this.tag = tag;
        this.owner = owner;
        this.members = new HashSet<>();
        this.members.add(owner);
        this.officers = new HashSet<>();
        this.experience = 0;
        this.level = 1;
    }

    public Guild(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.tag = (String) map.get("tag");
        this.owner = UUID.fromString((String) map.get("owner"));
        this.members = new HashSet<>();
        @SuppressWarnings("unchecked")
        List<String> rawMembers = (List<String>) map.get("members");
        if (rawMembers != null) {
            for (String s : rawMembers)
                members.add(UUID.fromString(s));
        }

        this.officers = new HashSet<>();
        @SuppressWarnings("unchecked")
        List<String> rawOfficers = (List<String>) map.get("officers");
        if (rawOfficers != null) {
            for (String s : rawOfficers)
                officers.add(UUID.fromString(s));
        }

        this.experience = (double) map.getOrDefault("experience", 0.0);
        this.level = (int) map.getOrDefault("level", 1);
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public UUID getOwner() {
        return owner;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Set<UUID> getOfficers() {
        return officers;
    }

    public double getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
        officers.remove(uuid);
    }

    public void addOfficer(UUID uuid) {
        officers.add(uuid);
    }

    public void removeOfficer(UUID uuid) {
        officers.remove(uuid);
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        addMember(uuid);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("tag", tag);
        map.put("owner", owner.toString());
        List<String> memberList = new ArrayList<>();
        for (UUID uuid : members)
            memberList.add(uuid.toString());
        map.put("members", memberList);
        List<String> officerList = new ArrayList<>();
        for (UUID uuid : officers)
            officerList.add(uuid.toString());
        map.put("officers", officerList);
        map.put("experience", experience);
        map.put("level", level);
        return map;
    }
}
