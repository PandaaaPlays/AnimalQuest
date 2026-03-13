package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ResetNpcsSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "resetnpcs";
    }

    @Override
    public String getDescription() {
        return "Resets all NPCs in the world based on the config.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest resetnpcs";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        AnimalQuest.getPlugin().getNpcManager().resetNpcs();
        sender.sendMessage(
                Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bAll NPCs have been reset successfully!"));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
