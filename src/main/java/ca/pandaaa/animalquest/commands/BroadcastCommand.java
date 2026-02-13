package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("animalquest.staff")) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInsufficient permission."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: /broadcast <message>"));
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        AnimalQuest.getPlugin().getStaffManager().broadcast(message.toString().trim());
        return true;
    }
}
