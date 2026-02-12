package ca.pandaaa.animalquest.player.rank;

import org.bukkit.entity.Player;

public enum StaffRank {
    ADMIN("animalquest.admin", "&#B00000&lA&#BF0D0D&ld&#CE1919&lm&#DC2626&li&#EB3232&ln"),
    MOD("animalquest.mod", "&#00A2A2&lM&#00D1D1&lo&#00FFFF&ld"),
    HELPER("animalquest.helper", "&#008F00&lH&#00A900&le&#00C300&ll&#00DD00&lp&#00F700&le&#11FF11&lr"),
    BUILDER("animalquest.builder", "&#9303C9&lB&#9E0BCF&lu&#AA13D4&li&#B51BDA&ll&#C022E0&ld&#CC2AE5&le&#D732EB&lr");

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
