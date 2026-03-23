package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class BlockRegenManager {
    private final AnimalQuest plugin;
    private final Set<BlockState> pendingRegens = new HashSet<>();

    public BlockRegenManager(AnimalQuest plugin) {
        this.plugin = plugin;
    }

    public void queueRegen(Block block, int seconds) {
        BlockState state = block.getState();
        pendingRegens.add(state);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingRegens.remove(state)) {
                    state.update(true, false);
                }
            }
        }.runTaskLater(plugin, seconds * 20L);
    }

    public void restoreAll() {
        for (BlockState state : pendingRegens) {
            state.update(true, false);
        }
        pendingRegens.clear();
    }
}

