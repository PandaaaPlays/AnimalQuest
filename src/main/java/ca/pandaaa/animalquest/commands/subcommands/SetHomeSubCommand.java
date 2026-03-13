package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetHomeSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getDescription() {
        return "Confirm setting your home.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest sethome <home>";
    }

    @Override
    public String getPermission() {
        return "animalquest.player";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player))
            return;
        if (args.length < 2)
            return;

        String subtype = args[1];
        if (!AnimalQuest.getPlugin().getNpcManager().consumePendingHome(player.getUniqueId(), subtype)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must click the Home NPC to perform this action!"));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data != null) {
            if (data.getHomeName().equalsIgnoreCase(subtype)) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYour home is already set here!"));
                return;
            }
            data.setHome(subtype);
            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName()
                    + " &7&l>> &bHome updated successfully to &3" + Utils.getSentenceCase(subtype) + "&b!"));
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
