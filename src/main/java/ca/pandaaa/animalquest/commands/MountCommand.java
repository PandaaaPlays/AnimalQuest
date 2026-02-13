package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class MountCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return true;
        }

        List<Entity> targetEntities = player.getNearbyEntities(5, 5, 5);
        Entity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (Entity entity : targetEntities) {
            if (entity instanceof Player)
                continue;
            double dist = entity.getLocation().distanceSquared(player.getLocation());
            if (dist < closestDist) {
                closest = entity;
                closestDist = dist;
            }
        }

        if (closest != null) {
            closest.addPassenger(player);
            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + "&7&l>> &bYou are now riding a &f"
                    + closest.getType().name().toLowerCase() + "&b."));
        } else {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cNo entity nearby to mount."));
        }
        return true;
    }
}
