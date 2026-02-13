package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.StaffManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return true;
        }

        if (!player.hasPermission("animalquest.staff")) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cInsufficient permission."));
            return true;
        }

        StaffManager staffManager = AnimalQuest.getPlugin().getStaffManager();
        if (args.length == 0) {
            staffManager.toggleStaffChat(player);
        } else {
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }
            staffManager.sendStaffChatMessage(player, message.toString().trim());
        }
        return true;
    }
}
