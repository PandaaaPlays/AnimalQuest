package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.player.AnimalRank;
import ca.pandaaa.animalquest.player.StaffRank;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabManager {

    public void updateTablistHeader(Player player) {
        player.setPlayerListHeader(Utils.applyFormat(
                "\n" + Utils.getAnimalQuestName() + "\n&bOnline: &f" + Bukkit.getOnlinePlayers().size() + "\n"));
        player.setPlayerListFooter(
                Utils.applyFormat("\n&3&lDISCORD &fdiscord.io/AnimalQuest\n&b&lSTORE &fanimalquest.buycraft.net\n"));
    }

    public void updatePlayerTablistDisplay(Player player) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data == null)
            return;

        String rankPrefix = getRankPrefix(player);
        String level = Utils
                .applyFormat(" &8[" + data.getExperience().getLevelColor() + data.getExperience().getLevel() + "&8]");
        player.setPlayerListName(rankPrefix + player.getName() + level);
    }

    private String getRankPrefix(Player player) {
        StaffRank staffRank = StaffRank.getPlayerRank(player);
        if (staffRank != null) {
            return Utils.applyFormat(staffRank.getDisplayName() + " &f");
        } else {
            AnimalRank animalRank = AnimalRank.getPlayerRank(player);
            return animalRank != null ? Utils.applyFormat(animalRank.getDisplayName() + " &f") : "";
        }
    }
}
