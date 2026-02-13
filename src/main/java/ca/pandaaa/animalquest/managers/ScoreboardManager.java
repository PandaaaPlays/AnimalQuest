package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.Guild;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.Experience;
import ca.pandaaa.animalquest.enums.AnimalRank;
import ca.pandaaa.animalquest.enums.StaffRank;
import ca.pandaaa.animalquest.utils.GlobalMultiplier;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.animalquest.utils.Formats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {
    private final AnimalQuest plugin;
    private final PlayerDataManager playerDataManager;

    public ScoreboardManager(AnimalQuest plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    public void setupScoreboard(Player player) {
        PlayerData playerData = this.playerDataManager.get(player.getUniqueId());
        if (playerData == null) {
            Bukkit.getLogger().warning("Could not set up scoreboard for " + player.getName() + ": PlayerData is null.");
            return;
        }

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("AnimalQuest", Criteria.DUMMY, Utils.getAnimalQuestName());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(Utils.applyFormat("&r                        &r")).setScore(11);
        objective.getScore(Utils.applyFormat("&b&lStatistics")).setScore(10);
        objective.getScore(Utils.applyFormat("&r         ")).setScore(5);
        objective.getScore(Utils.applyFormat("&b&lInformations")).setScore(4);
        objective.getScore(Utils.applyFormat("&r             ")).setScore(1);

        createTeam(board, "playerLevel", ChatColor.RED.toString(), 9);
        createTeam(board, "playerProgress", ChatColor.GOLD.toString(), 8);
        createTeam(board, "playerExperience", ChatColor.YELLOW.toString(), 7);
        createTeam(board, "playerMoney", ChatColor.GREEN.toString(), 6);
        createTeam(board, "playerGuild", ChatColor.DARK_GREEN.toString(), 3);
        createTeam(board, "bonus", ChatColor.DARK_AQUA.toString(), 2);

        player.setScoreboard(board);
        updateScoreboardData(board, playerData);
        updateTablist(player);
    }

    private void createTeam(Scoreboard board, String name, String entry, int score) {
        Team team = board.registerNewTeam(name);
        team.addEntry(entry);
        Objective objective = board.getObjective("AnimalQuest");
        if (objective != null) {
            objective.getScore(entry).setScore(score);
        }
    }

    public void updateScoreboard(Player player, boolean levelChanged) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("AnimalQuest");

        if (objective == null || objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
            setupScoreboard(player);
            return;
        }

        PlayerData playerData = this.playerDataManager.get(player.getUniqueId());
        if (playerData == null)
            return;

        updateScoreboardData(board, playerData);

        // Only update tablist when level actually changes
        if (levelChanged) {
            updateTablist(player);
        }
    }

    private void updateScoreboardData(Scoreboard board, PlayerData playerData) {
        Experience exp = playerData.getExperience();

        // Level
        Team levelTeam = board.getTeam("playerLevel");
        if (levelTeam != null) {
            levelTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bLevel &f" + exp.getLevel()));
        }

        // Progress
        double goal = exp.getGoalExperience();
        double progressPercent = goal > 0 ? (exp.getExperience() / goal) * 100 : 0;
        Team progressTeam = board.getTeam("playerProgress");
        if (progressTeam != null) {
            progressTeam.setPrefix(Utils
                .applyFormat(" &3&l⁎ &bProgress &f" + Formats.formatPercentage(progressPercent) + "%"));
        }

        // Experience
        Team experienceTeam = board.getTeam("playerExperience");
        if (experienceTeam != null) {
            experienceTeam
                .setPrefix(Utils.applyFormat("  &3&l- &f" + Formats.formatExperienceScoreboard(exp.getExperience())
                    + " / " + Formats.formatExperienceScoreboard(goal)));
        }

        // Money
        Team moneyTeam = board.getTeam("playerMoney");
        if (moneyTeam != null) {
            moneyTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bMoney &f$" + Formats.formatMoney(playerData.getBalance())));
        }

        // Guild
        Team guildTeam = board.getTeam("playerGuild");
        if (guildTeam != null) {
            Guild guild = plugin.getGuildManager().getPlayerGuild(playerData.getUuid());
            String guildName = guild != null ? guild.getName() : "-";
            guildTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bGuild &f" + guildName));
        }

        // Multiplier
        Team bonusTeam = board.getTeam("bonus");
        if (bonusTeam != null) {
            double multiplier = GlobalMultiplier.getGlobalMultiplier();
            bonusTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bBonus &fx" + Formats.formatBonus(multiplier)));
        }
    }

    public void updateTablistHeader(Player player) {
        player.setPlayerListHeader(Utils.applyFormat("\n" + Utils.getAnimalQuestName() + "\n&bOnline: &f" + Bukkit.getOnlinePlayers().size() + "\n"));
        player.setPlayerListFooter(Utils.applyFormat("\n&3&lDISCORD &fdiscord.io/AnimalQuest\n&b&lSTORE &fanimalquest.buycraft.net\n"));
    }

    public void updateTablist(Player player) {
        PlayerData playerData = this.playerDataManager.get(player.getUniqueId());
        String rankPrefix = Utils.getRankPrefix(player);
        String level = Utils.applyFormat(" &8[" + playerData.getExperience().getLevelColor() + playerData.getExperience().getLevel() + "&8]");
        player.setPlayerListName(rankPrefix + player.getName() + level);
    }
}
