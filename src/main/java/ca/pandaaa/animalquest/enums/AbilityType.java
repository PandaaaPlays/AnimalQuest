package ca.pandaaa.animalquest.enums;

public enum AbilityType {
    DASH("&b&lDASH", "&7Quickly dash forward."),
    LEAP("&a&lLEAP", "&7Leap into the air."),
    LIGHTNING_STRIKE("&e&lLIGHTNING STRIKE", "&7Strikes lightning on hit."),
    HEALING_TOUCH("&d&lHEALING TOUCH", "&7Heals you when you hit an enemy.");

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
