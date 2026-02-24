package ca.pandaaa.animalquest.commands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.QuestsGUI;
import ca.pandaaa.animalquest.quests.Quest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Command handler for quest-related commands
 */
public class QuestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            // Open quest GUI
            new QuestsGUI().openMainMenu(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list":
                // List all active quests
                listActiveQuests(player);
                break;

            case "track":
                // Track a specific quest
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /quest track <questId>");
                    return true;
                }
                trackQuest(player, args[1]);
                break;

            case "untrack":
                // Untrack current quest
                untrackQuest(player);
                break;

            case "abandon":
                // Abandon a quest
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /quest abandon <questId>");
                    return true;
                }
                abandonQuest(player, args[1]);
                break;

            case "info":
                // Show quest info
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /quest info <questId>");
                    return true;
                }
                new QuestsGUI().openQuestDetails(player, args[1]);
                break;

            default:
                player.sendMessage("§cUnknown subcommand. Use /quest for the quest menu.");
                break;
        }

        return true;
    }

    /**
     * List all active quests
     */
    private void listActiveQuests(Player player) {
        var data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        var activeQuests = data.getQuests().getActiveQuests();

        if (activeQuests.isEmpty()) {
            player.sendMessage("§eYou have no active quests.");
            return;
        }

        player.sendMessage("§6§l━━━━━━━ Active Quests ━━━━━━━");
        for (var entry : activeQuests.entrySet()) {
            var quest = AnimalQuest.getPlugin().getQuestManager().getQuest(entry.getKey());
            if (quest != null) {
                var progress = entry.getValue();
                var currentStep = quest.getStep(progress.getCurrentStep());

                player.sendMessage("§e" + quest.getName() + " §7[" + (progress.getCurrentStep() + 1) + "/"
                        + quest.getTotalSteps() + "]");
                if (currentStep != null) {
                    player.sendMessage("  §f" + currentStep.getDescription() + " §7(" +
                            currentStep.getProgressString(progress.getCurrentProgress()) + ")");
                }
            }
        }
        player.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Track a quest
     */
    private void trackQuest(Player player, String questId) {
        var data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        if (!data.getQuests().hasActiveQuest(questId)) {
            player.sendMessage("§cYou don't have this quest active!");
            return;
        }

        data.getQuests().setTrackedQuestId(questId);
        var quest = AnimalQuest.getPlugin().getQuestManager().getQuest(questId);

        if (quest != null) {
            player.sendMessage("§aNow tracking: §f" + quest.getName());
            AnimalQuest.getPlugin().getQuestManager().updateQuestDisplay(player);
        }
    }

    /**
     * Untrack current quest
     */
    private void untrackQuest(Player player) {
        var data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        data.getQuests().setTrackedQuestId(null);
        player.sendMessage("§7Quest tracking disabled");
    }

    /**
     * Abandon a quest
     */
    private void abandonQuest(Player player, String questId) {
        var data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        if (!data.getQuests().hasActiveQuest(questId)) {
            player.sendMessage("§cYou don't have this quest active!");
            return;
        }

        AnimalQuest.getPlugin().getQuestManager().abandonQuest(player, questId);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("list");
            completions.add("track");
            completions.add("untrack");
            completions.add("abandon");
            completions.add("info");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("track") ||
                args[0].equalsIgnoreCase("abandon") ||
                args[0].equalsIgnoreCase("info"))) {
            if (sender instanceof Player player) {
                var data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
                completions.addAll(data.getQuests().getActiveQuests().keySet());
            }
        }

        return completions;
    }
}
