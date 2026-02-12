package ca.pandaaa.animalquest.player;

import org.bukkit.entity.Player;

public enum AnimalRank {
    FOX(1, "&#FF8C42&lF&#FFAB5C&lo&#FFCA76&lx", "animalquest.fox"),
    LYNX(2, "&#84746F&lL&#9E9390&ly&#B7B2B0&ln&#D1D1D1&lx", "animalquest.lynx"),
    BEAR(3, "&#5A3E1B&lB&#6A4923&le&#7B532C&la&#8B5E34&lr", "animalquest.bear"),
    TIGER(4, "&#FF6A00&lT&#FF8000&li&#FF9600&lg&#FFAB00&le&#FFC100&lr", "animalquest.tiger"),
    PHOENIX(5, "&#FF3B3B&lP&#FF5531&lh&#FF6F27&lo&#FF891E&le&#FFA314&ln&#FFBD0A&li&#FFD700&lx", "animalquest.phoenix");

    private final int level;
    private final String displayName;
    private final String permission;

    AnimalRank(int level, String displayName, String permission) {
        this.level = level;
        this.displayName = displayName;
        this.permission = permission;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPermission() {
        return permission;
    }
Q
    public static AnimalRank getPlayerRank(Player player) {
        AnimalRank[] ranks = values();
        for (int i = ranks.length - 1; i >= 0; i--) {
            if (player.hasPermission(ranks[i].permission)) {
                return ranks[i];
            }
        }
        return null;
    }
}
