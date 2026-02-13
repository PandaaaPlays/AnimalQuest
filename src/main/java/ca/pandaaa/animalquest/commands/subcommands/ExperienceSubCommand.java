package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.commands.subcommands.SubCommand;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Experience;

import org.bukkit.Bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExperienceSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "experience";
    }

    @Override
    public String getDescription() {
        return "Manage player experience and levels.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest experience <add|set|remove> <player> <level|exp> <amount>";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: " + getSyntax()));
            return;
        }

        String action = args[1].toLowerCase();
        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return;
        }

        String type = args[3].toLowerCase();
        if (!type.equals("level") && !type.equals("exp")) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid type. Use 'level' or 'exp'."));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[4]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid amount."));
            return;
        }

        if (type.equals("level") && amount != (int) amount) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cLevel amount must be a whole number."));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(target.getUniqueId());
        if (data == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer data not loaded."));
            return;
        }

        Experience playerExperience = data.getExperience();

        switch (action) {
            case "add":
                if (type.equals("level")) {
                    playerExperience.addLevel((int) amount);
                    sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bAdded " + (int) amount
                        + " level(s) to " + target.getName()));
                } else {
                    playerExperience.addExperience(amount);
                    sender.sendMessage(Utils.applyFormat(
                        Utils.getAnimalQuestName() + "&7&l>> &bAdded " + amount + " exp to " + target.getName()));
                }
                break;
            case "set":
                if (type.equals("level")) {
                    playerExperience.setLevel((int) amount);
                    sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bSet " + target.getName()
                        + "'s level to " + (int) amount));
                } else {
                    playerExperience.setExperience(amount);
                    sender.sendMessage(Utils.applyFormat(
                        Utils.getAnimalQuestName() + "&7&l>> &bSet " + target.getName() + "'s exp to " + amount));
                }
                break;
            case "remove":
                if (type.equals("level")) {
                    playerExperience.removeLevel((int) amount);
                    sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bRemoved " + (int) amount
                        + " level(s) from " + target.getName()));
                } else {
                    playerExperience.removeExperience(amount);
                    sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bRemoved " + amount
                        + " exp from " + target.getName()));
                }
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
        } else if (args.length == 4) {
            return Arrays.asList("level", "exp");
        }
        return null;
    }
}
