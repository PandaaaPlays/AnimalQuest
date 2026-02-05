package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public Commands() {
        registerSubCommand(new ExperienceSubCommand());
        registerSubCommand(new ManaSubCommand());
        registerSubCommand(new SpellSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.applyFormat(
                    "&8--- " + Utils.getAnimalQuestName() + " &8Help ---"));
            for (SubCommand sub : subCommands.values()) {
                if (sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage(ChatColor.GRAY + sub.getSyntax() + " - " + sub.getDescription());
                }
            }
            return true;
        }

        // Subcommand is the argument after /animalquest
        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUnknown command."));
            return true;
        }

        if (!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInsufficient permission."));
            return true;
        }

        subCommand.perform(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String subCommand : subCommands.keySet()) {
                if (subCommand.startsWith(args[0].toLowerCase()) && sender.hasPermission(subCommands.get(subCommand).getPermission())) {
                    completions.add(subCommand);
                }
            }
            return completions;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null && sender.hasPermission(subCommand.getPermission())) {
            return subCommand.getSubcommandArguments(sender, args);
        }

        return null;
    }

    private void registerSubCommand(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }
}
