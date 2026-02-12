package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.JobsGUI;
import ca.pandaaa.animalquest.jobs.Jobs;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JobsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return true;
        }

        Jobs jobs = AnimalQuest.getPlugin().getPlayerDataManager().loadPlayer(player.getUniqueId()).getJobs();
        new JobsGUI().openInventory(player, jobs);
        return true;
    }
}
