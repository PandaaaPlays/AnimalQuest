package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.shop.Shop;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShopSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public String getDescription() {
        return "Summons a shop NPC.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest shop [shopName|remove]";
    }

    @Override
    public String getPermission() {
        return "animalquest.admin";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: " + getSyntax()));
            return;
        }

        if (args[1].equalsIgnoreCase("kill") || args[1].equalsIgnoreCase("remove")) {
            AnimalQuest.getPlugin().getShopManager().removeShopNpc(player);
            return;
        }

        String shopName = args[1];
        Shop shop = AnimalQuest.getPlugin().getShopManager().getShop(shopName);
        if (shop == null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cShop not found: " + shopName));
            return;
        }

        AnimalQuest.getPlugin().getShopManager().summonShopNPC(player, shopName);
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> options = new ArrayList<>(AnimalQuest.getPlugin().getShopManager().getShops().keySet());
            options.add("kill");
            return options;
        }
        return new ArrayList<>();
    }
}
