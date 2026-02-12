package ca.pandaaa.animalquest.player;

import org.bukkit.entity.Player;

public enum StaffRank {
    ADMIN("animalquest.admin", "&#B00000&lA&#BD1515&ld&#CB2A2A&lm&#D83F3F&li&#E55454&ln"),
    MOD("animalquest.mod", "&#00A2A2&lM&#00D1D1&lo&#00FFFF&ld"),
    HELPER("animalquest.helper", "&#008F00&lH&#00A900&le&#00C300&ll&#00DD00&lp&#00F700&le&#11FF11&lr");

    private final String permission;
    private final String displayName;

    StaffRank(String permission, String displayName) {
        this.permission = permission;
        this.displayName = displayName;
    }

    public String getPermission() {
        return permission;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StaffRank getPlayerRank(Player player) {
        for (StaffRank rank : values()) {
            if (player.hasPermission(rank.permission)) {
                return rank;
            }
        }
        return null;
    }
}
