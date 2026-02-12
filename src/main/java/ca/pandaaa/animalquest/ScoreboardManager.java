package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.player.PlayerDataManager;
import ca.pandaaa.animalquest.player.experience.Experience;
import ca.pandaaa.animalquest.utils.Utils;
import ca.pandaaa.animalquest.utils.Formats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Criteria;

public class ScoreboardManager {

    private final PlayerDataManager playerDataManager;
    private final TabManager tabManager = new TabManager();

    public ScoreboardManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void setupScoreboard(Player player) {
        PlayerData playerData = this.playerDataManager.get(player.getUniqueId());
        if (playerData == null) {
            Bukkit.getLogger().warning("Could not set up scoreboard for " + player.getName() + ": PlayerData is null.");
            return;
        }

        org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective("AnimalQuest", Criteria.DUMMY,
                ca.pandaaa.animalquest.utils.Utils.getAnimalQuestName());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(Utils.applyFormat("&r                        &r")).setScore(11);
        objective.getScore(Utils.applyFormat("&b&lStatistics")).setScore(10);
        objective.getScore(Utils.applyFormat("&r         ")).setScore(5);
        objective.getScore(Utils.applyFormat("&b&lInformations")).setScore(4);
        objective.getScore(Utils.applyFormat("&r             ")).setScore(1);

        // Teams for dynamic updates
        createTeam(board, "playerLevel", ChatColor.RED.toString(), 9);
        createTeam(board, "playerProgress", ChatColor.GOLD.toString(), 8);
        createTeam(board, "playerExperience", ChatColor.YELLOW.toString(), 7);
        createTeam(board, "playerMoney", ChatColor.GREEN.toString(), 6);
        createTeam(board, "playerGuild", ChatColor.DARK_GREEN.toString(), 3);
        createTeam(board, "bonus", ChatColor.DARK_AQUA.toString(), 2);

        player.setScoreboard(board);

        updateBoard(board, playerData);
    }

    public void updateTablistHeader() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            tabManager.updateTablistHeader(player);
        }
    }

    public void updatePlayerTablistDisplay(Player player) {
        tabManager.updatePlayerTablistDisplay(player);
    }

    private void createTeam(org.bukkit.scoreboard.Scoreboard board, String name, String entry, int score) {
        Team team = board.registerNewTeam(name);
        team.addEntry(entry);
        Objective objective = board.getObjective("AnimalQuest");
        if (objective != null) {
            objective.getScore(entry).setScore(score);
        }
    }

    public void updateScoreboard(Player player) {
        updateScoreboard(player, false);
    }

    public void updateScoreboard(Player player, boolean levelChanged) {
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("AnimalQuest");

        if (objective == null || objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
            setupScoreboard(player);
            return;
        }

        PlayerData playerData = this.playerDataManager.get(player.getUniqueId());
        if (playerData == null)
            return;

        updateBoard(board, playerData);

        // Only update tablist when level actually changes
        if (levelChanged) {
            updatePlayerTablistDisplay(player);
        }
    }

    private void updateBoard(org.bukkit.scoreboard.Scoreboard board, PlayerData playerData) {
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
            guildTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bGuild &f-"));
        }

        // Multiplier
        Team bonusTeam = board.getTeam("bonus");
        if (bonusTeam != null) {
            double multiplier = ca.pandaaa.animalquest.AnimalQuest.getPlugin().getMultiplierManager()
                    .getGlobalMultiplier(); // TODO Shows 1.1x for admin?
            bonusTeam.setPrefix(Utils.applyFormat(" &3&l⁎ &bBonus &fx" + Formats.formatBonus(multiplier)));
        }
    }
}
