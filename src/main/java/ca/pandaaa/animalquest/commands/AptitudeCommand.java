package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.guis.AptitudesGUI;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AptitudeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cThis command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;
        AptitudesGUI gui = new AptitudesGUI();
        gui.openInventory(player);

        return true;
    }
}
