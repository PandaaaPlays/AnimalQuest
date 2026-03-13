package ca.pandaaa.animalquest.enums;

public enum AbilityType {
    DASH("&b&lDASH", "&7Quickly dash forward."),
    LIGHTNING_STRIKE("&e&lLIGHTNING STRIKE", "&7Strikes lightning on hit."),
    LIFESTEAL("&4&lLIFESTEAL", "&7Steals health from your enemies."),
    FIRE_AURA("&6&lFIRE AURA", "&7Sets nearby enemies on fire."),
    EXPLOSIVE_HIT("&c&lEXPLOSIVE HIT", "&7Chance to cause an explosion on hit.");

    private final String displayName;
    private final String description;

    AbilityType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
