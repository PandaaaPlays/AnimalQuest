package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class WorldResetManager {

    public static final String BACKUP_WORLD_NAME = "world_backup";
    public static final String PLAYER_WORLD_NAME = "world";

    private static final int RESET_HOUR = 2;
    private static final int RESET_MINUTE = 0;

    private final AnimalQuest plugin;
    private final Logger log;
    private BukkitTask schedulerTask;
    private boolean resetInProgress = false;

    public WorldResetManager(AnimalQuest plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();

        handleCrashRecovery();
        ensureWorldsExist();
        startDailyScheduler();
    }

    public boolean isResetInProgress() {
        return resetInProgress;
    }

    public void resetPlayerWorld(Runnable onComplete) {
        if (resetInProgress) {
            log.warning("&c&l[!] &cA reset is already in progress! Ignoring duplicate request.");
            return;
        }
        resetInProgress = true;

        broadcastCountdown(5, () -> {
            kickAllPlayers();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                try {
                    plugin.getDataFolder().mkdirs();
                    new File(plugin.getDataFolder(), "reset.lock").createNewFile();
                } catch (IOException e) {
                    log.severe("&c&l[!] &cCould not create reset.lock: " + e.getMessage());
                }

                World backupWorld = Bukkit.getWorld(BACKUP_WORLD_NAME);
                if (backupWorld != null) {
                    backupWorld.save();
                    Bukkit.unloadWorld(backupWorld, true);
                    log.info("&aBackup world archived for sync.");
                }

                World playerWorld = Bukkit.getWorld(PLAYER_WORLD_NAME);
                if (playerWorld != null) {
                    if (Bukkit.getWorlds().get(0).equals(playerWorld)) {
                        log.severe("&c&l[!] &cABORT: '" + PLAYER_WORLD_NAME + "' is the PRIMARY world.");
                        log.severe("&ePlease set 'level-name' in server.properties to 'lobby' and restart.");
                        cleanupReset();
                        ensureWorldsExist();
                        return;
                    }

                    boolean unloaded = Bukkit.unloadWorld(playerWorld, true);
                    if (!unloaded) {
                        log.severe("&c&l[!] &cFailed to unload player world. Reset aborted.");
                        cleanupReset();
                        ensureWorldsExist();
                        return;
                    }
                }

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        copyWorld(BACKUP_WORLD_NAME, PLAYER_WORLD_NAME);
                    } catch (IOException e) {
                        log.severe("&c&l[!] &cCopy failed: " + e.getMessage());
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            cleanupReset();
                            ensureWorldsExist();
                        });
                        return;
                    }

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        WorldCreator backupCreator = new WorldCreator(BACKUP_WORLD_NAME);
                        Bukkit.createWorld(backupCreator);

                        WorldCreator playerCreator = new WorldCreator(PLAYER_WORLD_NAME);
                        Bukkit.createWorld(playerCreator);

                        cleanupReset();
                        Bukkit.getConsoleSender().sendMessage(Utils.applyFormat(
                                Utils.getAnimalQuestName() + " &7>> &bPlayer world reset complete."));
                        if (onComplete != null)
                            onComplete.run();
                        AnimalQuest.getPlugin().getNpcManager().resetNpcs();
                    });
                });
            }, 10L);
        });
    }

    private void cleanupReset() {
        resetInProgress = false;
        new File(plugin.getDataFolder(), "reset.lock").delete();
    }

    private void handleCrashRecovery() {
        File lockFile = new File(plugin.getDataFolder(), "reset.lock");
        if (!lockFile.exists())
            return;

        log.warning("&c&l[!] &creset.lock detected – server likely crashed during a reset. Recovering from backup...");
        try {
            copyWorld(BACKUP_WORLD_NAME, PLAYER_WORLD_NAME);
            log.info("&aRecovery complete.");
        } catch (IOException e) {
            log.severe("&c&l[!] &cRecovery copy failed: " + e.getMessage());
        } finally {
            lockFile.delete();
        }
    }

    private void ensureWorldsExist() {
        if (Bukkit.getWorld(BACKUP_WORLD_NAME) == null) {
            File backupFolder = new File(Bukkit.getWorldContainer(), BACKUP_WORLD_NAME);
            if (backupFolder.exists()) {
                Bukkit.createWorld(new WorldCreator(BACKUP_WORLD_NAME));
                log.info("&aBackup world loaded: " + BACKUP_WORLD_NAME);
            } else {
                log.warning("&c&l[!] &cBackup world folder '" + BACKUP_WORLD_NAME
                        + "' not found – please create it manually and restart the server.");
            }
        }

        if (Bukkit.getWorld(PLAYER_WORLD_NAME) == null) {
            File playerFolder = new File(Bukkit.getWorldContainer(), PLAYER_WORLD_NAME);
            if (!playerFolder.exists()) {
                File backupFolder = new File(Bukkit.getWorldContainer(), BACKUP_WORLD_NAME);
                if (backupFolder.exists()) {
                    log.info("&c&l[!] &cPlayer world not found – creating initial copy from backup.");
                    try {
                        copyWorld(BACKUP_WORLD_NAME, PLAYER_WORLD_NAME);
                    } catch (IOException e) {
                        log.severe("&c&l[!] &cInitial copy failed: " + e.getMessage());
                        return;
                    }
                } else {
                    log.warning("&c&l[!] &cCannot create player world: backup world folder is missing.");
                    return;
                }
            }
            Bukkit.createWorld(new WorldCreator(PLAYER_WORLD_NAME));
            log.info("&aPlayer world loaded: " + PLAYER_WORLD_NAME);
        }
    }

    private void startDailyScheduler() {
        long delaySeconds = secondsUntilNextReset();
        long periodTicks = TimeUnit.DAYS.toSeconds(1) * 20L;
        long delayTicks = delaySeconds * 20L;

        log.info(String.format("&bDaily reset scheduled in %d h %d m.",
                delaySeconds / 3600, (delaySeconds % 3600) / 60));

        schedulerTask = new BukkitRunnable() {
            @Override
            public void run() {
                log.info("&bDaily automatic reset triggered.");
                resetPlayerWorld(null);
            }
        }.runTaskTimer(plugin, delayTicks, periodTicks);
    }

    private long secondsUntilNextReset() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime next = now.toLocalDate().atTime(LocalTime.of(RESET_HOUR, RESET_MINUTE))
                .atZone(ZoneId.systemDefault());
        if (!next.isAfter(now)) {
            next = next.plusDays(1);
        }
        return next.toEpochSecond() - now.toEpochSecond();
    }

    private void kickAllPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(Utils.applyFormat(
                    "&c&lWorld resetting\n&7Please reconnect in a moment!"));
        }
    }

    private void broadcastCountdown(int seconds, Runnable action) {
        new BukkitRunnable() {
            int remaining = seconds;

            @Override
            public void run() {
                if (remaining > 0) {
                    Bukkit.broadcastMessage(Utils.applyFormat(
                            Utils.getAnimalQuestName() + " &7&l>> &bWorld resetting in &3" + remaining
                                    + " &bsecond(s)!"));
                    remaining--;
                } else {
                    cancel();
                    action.run();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void copyWorld(String sourceName, String destName) throws IOException {
        Path sourceDir = Bukkit.getWorldContainer().toPath().resolve(sourceName);
        Path destDir = Bukkit.getWorldContainer().toPath().resolve(destName);
        Path tempDir = plugin.getDataFolder().toPath().resolve("temp_playerdata");

        // 1. Stash: If the current world has playerdata, move it to a safe temp spot.
        Path activePlayerData = destDir.resolve("playerdata");
        if (Files.exists(activePlayerData)) {
            if (Files.exists(tempDir))
                deleteDirectory(tempDir);
            Files.move(activePlayerData, tempDir);
        }

        // 2. Wipe & Copy: Delete the entire destination and copy the source 1:1.
        if (Files.exists(destDir)) {
            deleteDirectory(destDir);
        }

        Files.walkFileTree(sourceDir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path target = destDir.resolve(sourceDir.relativize(dir));
                Files.createDirectories(target);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.getFileName().toString();
                if (fileName.equals("session.lock") || fileName.equals("uid.dat")) {
                    return FileVisitResult.CONTINUE;
                }
                Path target = destDir.resolve(sourceDir.relativize(file));
                Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });

        // 3. Restore: Remove the template's playerdata and bring back the stashed
        // active data.
        Path copiedPlayerData = destDir.resolve("playerdata");
        if (Files.exists(copiedPlayerData)) {
            deleteDirectory(copiedPlayerData);
        }
        if (Files.exists(tempDir)) {
            Files.move(tempDir, copiedPlayerData);
        }

        log.info("&bCopied and restored world state: '" + sourceName + "' -> '" + destName + "'.");
    }

    private void deleteDirectory(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path d, IOException exc) throws IOException {
                Files.delete(d);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public void shutdown() {
        if (schedulerTask != null && !schedulerTask.isCancelled()) {
            schedulerTask.cancel();
        }
    }
}
