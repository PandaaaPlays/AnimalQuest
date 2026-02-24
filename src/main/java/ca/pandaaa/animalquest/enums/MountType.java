package ca.pandaaa.animalquest.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public enum MountType {
    A(EntityType.HORSE, false, 1, 0.225, 0.7, null, Horse.Color.WHITE, Horse.Style.NONE, 60),
    B(EntityType.HORSE, false, 2, 0.26, 0.8, Material.IRON_HORSE_ARMOR, Horse.Color.CHESTNUT, Horse.Style.NONE, 45),
    C(EntityType.HORSE, false, 3, 0.3, 0.9, Material.GOLDEN_HORSE_ARMOR, Horse.Color.BLACK, Horse.Style.WHITE_DOTS, 30),
    D(EntityType.HORSE, false, 4, 0.35, 1.0, Material.DIAMOND_HORSE_ARMOR, Horse.Color.GRAY, Horse.Style.BLACK_DOTS,
            15),
    X(EntityType.NAUTILUS, true, 1, 0.2, 0.0, null, null, null, 60),
    Y(EntityType.ZOMBIE_NAUTILUS, true, 2, 0.3, 0.0, null, null, null, 30);

    private final EntityType type;
    private final boolean inWater;
    private final int tier;
    private final double speed;
    private final double jumpStrength;
    private final Material armor;
    private final Horse.Color color;
    private final Horse.Style style;
    private final int cooldown;

    MountType(EntityType type, boolean water, int tier, double speed, double jumpStrength, Material armor,
            Horse.Color color, Horse.Style style, int cooldown) {
        this.type = type;
        this.inWater = water;
        this.tier = tier;
        this.speed = speed;
        this.jumpStrength = jumpStrength;
        this.armor = armor;
        this.color = color;
        this.style = style;
        this.cooldown = cooldown;
    }

    public EntityType getType() {
        return type;
    }

    public boolean isInWater() {
        return inWater;
    }

    public int getTier() {
        return tier;
    }

    public double getSpeed() {
        return speed;
    }

    public double getJumpStrength() {
        return jumpStrength;
    }

    public Material getArmor() {
        return armor;
    }

    public Horse.Color getColor() {
        return color;
    }

    public Horse.Style getStyle() {
        return style;
    }

    public int getCooldown() {
        return cooldown;
    }
}
