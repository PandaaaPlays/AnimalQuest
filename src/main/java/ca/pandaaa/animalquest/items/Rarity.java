package ca.pandaaa.animalquest.items;

import org.bukkit.ChatColor;

public enum Rarity {
    NONE(ChatColor.DARK_PURPLE, "None"),
    COMMON(ChatColor.WHITE, "Common"),
    UNCOMMON(ChatColor.AQUA, "Uncommon"),
    RARE(ChatColor.DARK_AQUA, "Rare"),
    EPIC(ChatColor.LIGHT_PURPLE, "Epic"),
    LEGENDARY(ChatColor.YELLOW, "Legendary"),
    MYTHICAL(ChatColor.RED, "Mythical");

    private final ChatColor color;
    private final String displayName;

    Rarity(ChatColor color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }
}
