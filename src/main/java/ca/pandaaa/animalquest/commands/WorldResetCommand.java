package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.WorldResetManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorldResetCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("animalquest.admin") || !sender.hasPermission("animalquest.builder")) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cYou don't have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        WorldResetManager mgr = AnimalQuest.getPlugin().getWorldResetManager();

        switch (args[0].toLowerCase()) {
            case "reset" -> {
                if (mgr.isResetInProgress()) {
                    sender.sendMessage(Utils.applyFormat("&c&l[!] &cA reset is already in progress!"));
                    return true;
                }
                sender.sendMessage(Utils.applyFormat(
                        Utils.getAnimalQuestName() + " &7&l>> &bInitiating player world reset..."));
                mgr.resetPlayerWorld(
                        () -> sender.sendMessage(Utils.applyFormat(
                                Utils.getAnimalQuestName() + " &7&l>> &bReset complete!")));
            }

            case "switchto" -> {
                if (args.length < 2) {
                    sender.sendMessage(
                            Utils.applyFormat("&c&l[!] &cUsage: /worldreset switchto <backup|player> [playerName]"));
                    return true;
                }
                String targetWorld = resolveWorldName(args[1]);
                if (targetWorld == null) {
                    sender.sendMessage(Utils.applyFormat(
                            "&c&l[!] &cUnknown world '" + args[1] + "'. Use 'backup' or 'player'."));
                    return true;
                }
                World world = Bukkit.getWorld(targetWorld);
                if (world == null) {
                    sender.sendMessage(Utils.applyFormat(
                            "&c&l[!] &cWorld '" + targetWorld + "' is not loaded on this server."));
                    return true;
                }

                Player target;
                if (args.length >= 3) {
                    // Admin specified a player name
                    target = Bukkit.getPlayerExact(args[2]);
                    if (target == null) {
                        sender.sendMessage(Utils.applyFormat(
                                "&c&l[!] &cPlayer '" + args[2] + "' is not online."));
                        return true;
                    }
                } else {
                    // No name given – switch the sender themselves
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.applyFormat(
                                "&c&l[!] &cConsole must specify a player: /worldreset switchto <world> <playerName>"));
                        return true;
                    }
                    target = (Player) sender;
                }

                Location current = target.getLocation();
                Location dest = new Location(world, current.getX(), current.getY(), current.getZ(),
                        current.getYaw(), current.getPitch());
                target.teleport(dest);
                target.sendMessage(Utils.applyFormat(
                        Utils.getAnimalQuestName() + " &7&l>> &bSwitched to world: &3" + args[1]));
                if (target != sender) {
                    sender.sendMessage(Utils.applyFormat(
                            Utils.getAnimalQuestName() + " &7&l>> &bSwitched &3" + target.getName()
                                    + " &bto world: &3" + args[1]));
                }
            }

            case "status" -> {
                Player statusTarget;
                if (args.length >= 2) {
                    statusTarget = Bukkit.getPlayerExact(args[1]);
                    if (statusTarget == null) {
                        sender.sendMessage(Utils.applyFormat(
                                "&c&l[!] &cPlayer '" + args[1] + "' is not online."));
                        return true;
                    }
                } else {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.applyFormat(
                                "&c&l[!] &cConsole must specify a player: /worldreset status <playerName>"));
                        return true;
                    }
                    statusTarget = (Player) sender;
                }

                sender.sendMessage(Utils.applyFormat(
                        Utils.getAnimalQuestName() + " &7&l>> &b" + statusTarget.getName()
                                + " &7is in world: &3" + statusTarget.getWorld().getName()));
            }

            default -> sendHelp(sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("animalquest.admin"))
            return null;

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (String sub : List.of("reset", "switchto", "status")) {
                if (sub.startsWith(partial))
                    completions.add(sub);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("switchto")) {
            String partial = args[1].toLowerCase();
            for (String w : List.of("backup", "player")) {
                if (w.startsWith(partial))
                    completions.add(w);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            String partial = args[1].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(partial))
                    completions.add(p.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("switchto")) {
            String partial = args[2].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(partial))
                    completions.add(p.getName());
            }
        }

        return completions;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Utils.applyFormat("&8&l--- &3&lWorldReset Help &8&l---"));
        sender.sendMessage(Utils.applyFormat("&b/worldreset reset &8» &7Force-reset the player world now."));
        sender.sendMessage(Utils.applyFormat(
                "&b/worldreset switchto <backup|player> [player] &8» &7Teleport yourself (or someone) to a world."));
        sender.sendMessage(Utils.applyFormat("&b/worldreset status &8» &7Show the current world of the player."));
    }

    private String resolveWorldName(String input) {
        return switch (input.toLowerCase()) {
            case "backup" -> WorldResetManager.BACKUP_WORLD_NAME;
            case "player" -> WorldResetManager.PLAYER_WORLD_NAME;
            default -> null;
        };
    }
}
