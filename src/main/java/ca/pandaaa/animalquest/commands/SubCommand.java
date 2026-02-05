package ca.pandaaa.animalquest.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getName();
    String getDescription();
    String getSyntax();
    String getPermission();
    void perform(CommandSender sender, String[] args);
    List<String> getSubcommandArguments(CommandSender sender, String[] args);
}
