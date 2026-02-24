package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.Guild;
import ca.pandaaa.animalquest.managers.GuildManager;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuildCommand implements CommandExecutor, TabCompleter {
    private final GuildManager guildManager;

    public GuildCommand() {
        this.guildManager = AnimalQuest.getPlugin().getGuildManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Utils.applyFormat("&c&l[!] &cOnly players can use this command."));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(
                    Utils.applyFormat("&c&l[!] &cUsage: /guild <create|disband|invite|accept|leave|kick> [args]"));
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "create":
                handleCreate(player, args);
                break;
            case "disband":
                handleDisband(player);
                break;
            case "invite":
                handleInvite(player, args);
                break;
            case "accept":
                handleAccept(player, args);
                break;
            case "leave":
                handleLeave(player);
                break;
            case "kick":
                handleKick(player, args);
                break;
            default:
                player.sendMessage(Utils.applyFormat("&c&l[!] &cUnknown guild action."));
        }
        return true;
    }

    private void handleCreate(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: /guild create <name> <tag>"));
            return;
        }
        if (guildManager.getPlayerGuild(player.getUniqueId()) != null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou are already in a guild."));
            return;
        }
        String name = args[1];
        String tag = args[2];
        if (guildManager.guildExists(name)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cA guild with that name already exists."));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data.getBalance() < 50000) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou need $50,000 to create a guild."));
            return;
        }
        data.setBalance(data.getBalance() - 50000);

        guildManager.createGuild(name, tag, player.getUniqueId());
        player.sendMessage(Utils.applyFormat(
                Utils.getAnimalQuestName() + " &7&l>> &bGuild &f" + name + " &8[&f" + tag + "&8] &bcreated!"));
    }

    private void handleDisband(Player player) {
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null || !guild.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cOnly the guild owner can disband the guild."));
            return;
        }
        guildManager.deleteGuild(guild.getName());
        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &cGuild disbanded."));
    }

    private void handleInvite(Player player, String[] args) {
        player.sendMessage(Utils
                .applyFormat("&c&l[!] &cInvite system coming soon (currently direct invite via accept [guildname])."));
    }

    private void handleAccept(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: /guild accept <guildname>"));
            return;
        }
        if (guildManager.getPlayerGuild(player.getUniqueId()) != null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou are already in a guild."));
            return;
        }
        guildManager.joinGuild(player.getUniqueId(), args[1]);
        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bJoined guild " + args[1]));
    }

    private void handleLeave(Player player) {
        if (guildManager.getPlayerGuild(player.getUniqueId()) == null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou are not in a guild."));
            return;
        }
        guildManager.leaveGuild(player.getUniqueId());
        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &cYou left the guild."));
    }

    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cUsage: /guild kick <player>"));
            return;
        }
        Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
        if (guild == null || (!guild.getOwner().equals(player.getUniqueId())
                && !guild.getOfficers().contains(player.getUniqueId()))) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cOnly officers or the owner can kick members."));
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cPlayer not found."));
            return;
        }
        guildManager.leaveGuild(target.getUniqueId());
        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &cKicked " + target.getName()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "disband", "invite", "accept", "leave", "kick");
        }
        return Collections.emptyList();
    }
}
