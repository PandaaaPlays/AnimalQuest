package ca.pandaaa.animalquest;

import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabManager {

    public void updateTablistHeader(Player player) {
        player.setPlayerListHeader(Utils.applyFormat("\n" + Utils.getAnimalQuestName() + "\n&bOnline: &f" + Bukkit.getOnlinePlayers().size() + "\n"));
        player.setPlayerListFooter(Utils.applyFormat("\n&3&lDISCORD &fdiscord.io/AnimalQuest\n&b&lSTORE &fanimalquest.buycraft.net\n"));
    }

    public void updatePlayerTablistDisplay(Player player) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data == null)
            return;

        String rank = getRankPrefix(player);
        String level = Utils.applyFormat(" &8[" + data.getExperience().getLevelColor() + data.getExperience().getLevel() + "&8]");
        player.setPlayerListName(rank + player.getName() + level);
    }

    private String getRankPrefix(Player player) {
        if (player.hasPermission("AnimalQuest.admin")) {
            return Utils.applyFormat("&4&lAdmin &f");
        } else if (player.hasPermission("AnimalQuest.mod")) {
            return Utils.applyFormat("&c&lMod &f");
        } else if (player.hasPermission("AnimalQuest.helper")) {
            return Utils.applyFormat("&c&lHelper &f");
        } else {
            return Utils.applyFormat("&f");
        }
    }
}
