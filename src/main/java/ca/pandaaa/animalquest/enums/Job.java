package ca.pandaaa.animalquest.enums;

import org.bukkit.Material;

public enum Job {
    LUMBERJACK("&2&lLumberjack", Material.DIAMOND_AXE, "&7Master of the forest and timber."),
    MINER("&7&lMiner", Material.DIAMOND_PICKAXE, "&7Expert in subterranean caves and mines."),
    ALCHEMIST("&d&lAlchemist", Material.BREWING_STAND, "&7Concoctor of mystical brews."),
    EXPLORER("&e&lExplorer", Material.LANTERN, "&7Wanderer of the vast world and dungeons.");

    private final String displayName;
    private final Material icon;
    private final String description;

    Job(String displayName, Material icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }
}
