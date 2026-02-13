package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Formats;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: /pay <player> <amount>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou cannot pay yourself."));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid amount."));
            return true;
        }

        if (amount <= 0) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cAmount must be greater than 0."));
            return true;
        }

        PlayerData playerData = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        PlayerData targetData = AnimalQuest.getPlugin().getPlayerDataManager().get(target.getUniqueId());

        if (playerData.getBalance() < amount) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cInsufficient balance."));
            return true;
        }

        playerData.setBalance(playerData.getBalance() - amount);
        targetData.setBalance(targetData.getBalance() + amount);

        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bYou paid &f$"
                + Formats.formatMoney(amount) + " &bto &f" + target.getName() + "&b."));
        target.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bYou received &f$"
                + Formats.formatMoney(amount) + " &bfrom &f" + player.getName() + "&b."));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }
        return Collections.emptyList();
    }
}
