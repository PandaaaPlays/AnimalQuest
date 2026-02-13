package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.commands.subcommands.SubCommand;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalanceSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Manage player balance.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest balance <add|set|remove> <player> <amount>";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: " + getSyntax()));
            return;
        }

        String action = args[1].toLowerCase();
        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid amount."));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(target.getUniqueId());
        if (data == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer data not loaded."));
            return;
        }

        switch (action) {
            case "add":
                data.setBalance(data.getBalance() + amount);
                sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &8&l>> &bAdded &3$" + amount
                    + " &bto &3" + target.getName() + "&b's balance."));
                break;
            case "set":
                data.setBalance(amount);
                sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &8&l>> &bSet &3" + target.getName()
                    + "&b's balance to &3$" + amount + "&b."));
                break;
            case "remove":
                data.setBalance(Math.max(0, data.getBalance() - amount));
                sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &8&l>> &bRemoved &3$" + amount
                    + " &bfrom &3" + target.getName() + "&b's balance."));
                break;
            default:
                sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid action. Use add, set or remove."));
                return;
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("add", "set", "remove");
        } else if (args.length == 3) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return new ArrayList<>();
    }
}
