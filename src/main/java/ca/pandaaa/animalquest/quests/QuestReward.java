package ca.pandaaa.animalquest.quests;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.player.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents rewards given upon quest completion
 */
public class QuestReward {
    private final int experience;
    private final int money;
    private final List<ItemStack> items;

    public QuestReward(int experience, int money, List<ItemStack> items) {
        this.experience = experience;
        this.money = money;
        this.items = items != null ? items : new ArrayList<>();
    }

    public int getExperience() {
        return experience;
    }

    public int getMoney() {
        return money;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Give the rewards to a player
     */
    public void giveRewards(Player player) {
        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());

        // Give experience
        if (experience > 0) {
            data.getExperience().addExperience(experience);
            player.sendMessage("§a+ " + experience + " Experience");
        }

        // Give money
        if (money > 0) {
            data.setBalance(data.getBalance() + money);
            player.sendMessage("§6+ $" + money);
        }

        // Give items
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                player.getInventory().addItem(item.clone());
                player.sendMessage("§a+ " + item.getAmount() + "x " + item.getType().name());
            }
        }
    }

    public List<String> getRewardDescription() {
        List<String> description = new ArrayList<>();
        description.add("§6§lRewards:");

        if (experience > 0) {
            description.add("§e  +" + experience + " Experience");
        }

        if (money > 0) {
            description.add("§6  +$" + money);
        }

        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                description.add("§a  +" + item.getAmount() + "x " + item.getType().name());
            }
        }

        return description;
    }
}
