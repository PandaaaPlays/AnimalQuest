package ca.pandaaa.animalquest.player;

import ca.pandaaa.animalquest.enums.MountType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Mount implements ConfigurationSerializable {

    private MountType waterMount;
    private MountType groundMount;

    public Mount() {
        waterMount = MountType.ZOEY;
        groundMount = MountType.CROISSANT;
    }

    public Mount(Map<String, Object> map) {
        try {
            this.groundMount = MountType.valueOf((String) map.getOrDefault("ground-mount", "CROISSANT"));
        } catch (IllegalArgumentException e) {
            this.groundMount = MountType.CROISSANT;
        }

        try {
            this.waterMount = MountType.valueOf((String) map.getOrDefault("water-mount", "ZOEY"));
        } catch (IllegalArgumentException e) {
            this.waterMount = MountType.ZOEY;
        }
    }

    public MountType getWaterMount() {
        return waterMount;
    }

    public MountType getGroundMount() {
        return groundMount;
    }

    public void setWaterMount(MountType waterMount) {
        this.waterMount = waterMount;
    }

    public void setGroundMount(MountType groundMount) {
        this.groundMount = groundMount;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("water-mount", waterMount.name());
        map.put("ground-mount", groundMount.name());

        return map;
    }
}
