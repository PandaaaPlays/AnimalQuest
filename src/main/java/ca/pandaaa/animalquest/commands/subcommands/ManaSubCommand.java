package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Mana;

import org.bukkit.Bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManaSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "mana";
    }

    @Override
    public String getDescription() {
        return "Manage player mana.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest mana <add|set|remove> <player> <amount>";
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

        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid amount."));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(target.getUniqueId());
        if (data == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer data not loaded."));
            return;
        }

        Mana mana = data.getMana();
        switch (action) {
            case "add":
                mana.addMana(amount);
                sender.sendMessage(Utils.applyFormat(
                    Utils.getAnimalQuestName() + "&7&l>> &bAdded " + amount + " mana to " + target.getName()));
                break;
            case "set":
                mana.setCurrentMana(amount);
                sender.sendMessage(Utils.applyFormat(
                    Utils.getAnimalQuestName() + "&7&l>> &bSet " + target.getName() + "'s mana to " + amount));
                break;
            case "remove":
                mana.consumeMana(amount);
                sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bRemoved " + amount
                    + " mana from " + target.getName()));
                break;
            default:
                sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid action. Use add, set or remove."));
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
        return null;
    }
}
