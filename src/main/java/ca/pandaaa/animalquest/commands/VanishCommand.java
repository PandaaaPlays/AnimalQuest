package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.VanishManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

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

        VanishManager vanishManager = AnimalQuest.getPlugin().getVanishManager();
        boolean isVanished = vanishManager.isVanished(player);
        vanishManager.setVanished(player, !isVanished);

        if (!isVanished) {
            player.sendMessage(Utils.applyFormat("&8[&b&lAQ&8] &7Vanish &aon."));
        } else {
            player.sendMessage(Utils.applyFormat("&8[&b&lAQ&8] &7Vanish &coff."));
        }
        return true;
    }
}
