package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.enums.AnimalQuestItem;
import ca.pandaaa.animalquest.enums.Job;
import ca.pandaaa.animalquest.managers.JobRewardManager;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveSubCommand implements SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Give custom items to players.";
    }

    @Override
    public String getSyntax() {
        return "/aq give <player> <item> [amount/stars]";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: " + getSyntax()));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return;
        }

        String itemArg = args[2].toUpperCase();
        ItemStack item;
        String itemName;

        if (itemArg.equals("BRANCH") || itemArg.equals("GEM")) {
            Job job = itemArg.equals("BRANCH") ? Job.LUMBERJACK : Job.MINER;
            int stars = 1;
            if (args.length >= 4) {
                try {
                    stars = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid star level."));
                    return;
                }
            }
            item = JobRewardManager.getRareItem(job, stars);
            itemName = (job == Job.MINER ? "Rare Gem" : "Special Branch") + " (★" + stars + ")";
        } else {
            AnimalQuestItem itemType;
            try {
                itemType = AnimalQuestItem.valueOf(itemArg);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(
                        Utils.applyFormat("&c&l[!] &cInvalid item type. Use BRANCH, GEM or an AnimalQuestItem name."));
                return;
            }

            int amount = 1;
            if (args.length >= 4) {
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid amount."));
                    return;
                }
            }
            item = itemType.getItemStack(amount);
            itemName = itemType.getName();
        }

        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItemNaturally(target.getLocation(), item);
        } else {
            target.getInventory().addItem(item);
        }

        sender.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bGave &3" + item.getAmount() + "x "
                + itemName + " &bto &3" + target.getName()));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers())
                names.add(p.getName());
            return names;
        } else if (args.length == 3) {
            List<String> items = new ArrayList<>();
            items.add("BRANCH");
            items.add("GEM");
            for (AnimalQuestItem item : AnimalQuestItem.values()) {
                items.add(item.name());
            }
            return items;
        } else if (args.length == 4) {
            String itemArg = args[2].toUpperCase();
            if (itemArg.equals("BRANCH") || itemArg.equals("GEM")) {
                List<String> stars = new ArrayList<>();
                for (int i = 1; i <= 10; i++)
                    stars.add(String.valueOf(i));
                return stars;
            }
        }
        return null;
    }
}
