package ca.pandaaa.animalquest.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Cuboid {
    private final World world;
    private final int minX, minY, minZ;
    private final int maxX, maxY, maxZ;

    public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = world;
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public boolean contains(Location loc) {
        if (loc.getWorld() == null || !loc.getWorld().equals(world)) return false;
        return loc.getBlockX() >= minX && loc.getBlockX() <= maxX &&
               loc.getBlockY() >= minY && loc.getBlockY() <= maxY &&
               loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }

    public World getWorld() {
        return world;
    }
}
