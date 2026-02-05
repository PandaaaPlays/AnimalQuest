package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.animalquest.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpellSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "spell";
    }

    @Override
    public String getDescription() {
        return "Manage spells.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest spell give <player> <spell>";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 4 || !args[1].equalsIgnoreCase("give")) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: " + getSyntax()));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return;
        }

        Spell spell = AnimalQuest.getPlugin().getSpellManager().getSpellById(args[3]);
        if (spell == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cSpell not found. Registered spells: "
                    + String.join(", ", AnimalQuest.getPlugin().getSpellManager().getRegisteredSpells().keySet())));
            return;
        }

        target.getInventory().addItem(spell.getItem());
        sender.sendMessage(Utils.applyFormat(
                Utils.getAnimalQuestName() + "&7&l>> &bGave " + spell.getName() + " to " + target.getName()));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("give");
        } else if (args.length == 3) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        } else if (args.length == 4) {
            return new ArrayList<>(AnimalQuest.getPlugin().getSpellManager().getRegisteredSpells().keySet());
        }
        return null;
    }
}
