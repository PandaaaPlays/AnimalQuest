package ca.pandaaa.animalquest.listeners;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.quests.QuestObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listens to player actions and updates quest progress
 */
public class QuestListener implements Listener {
    private final AnimalQuest plugin;

    public QuestListener(AnimalQuest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        if (!(event.getEntity().getKiller() instanceof Player))
            return;
        Player player = (Player) event.getEntity().getKiller();

        String entityType = event.getEntityType().name();
        plugin.getQuestManager().updateQuestProgress(player, QuestObjective.ObjectiveType.KILL_ENTITY, entityType, 1);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String blockType = event.getBlock().getType().name();
        plugin.getQuestManager().updateQuestProgress(player, QuestObjective.ObjectiveType.BREAK_BLOCK, blockType, 1);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String blockType = event.getBlock().getType().name();
        plugin.getQuestManager().updateQuestProgress(player, QuestObjective.ObjectiveType.PLACE_BLOCK, blockType, 1);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();

        ItemStack result = event.getRecipe().getResult();
        String itemType = result.getType().name();
        int amount = result.getAmount();

        // Handle shift-click crafting
        if (event.isShiftClick()) {
            // Calculate how many items can be crafted
            amount = getMaxCraftAmount(event);
        }

        plugin.getQuestManager().updateQuestProgress(player, QuestObjective.ObjectiveType.CRAFT_ITEM, itemType,
                amount);
    }

    /**
     * Calculate maximum craft amount for shift-click
     */
    private int getMaxCraftAmount(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        int maxCraftable = Integer.MAX_VALUE;

        // Check each ingredient
        for (ItemStack ingredient : event.getInventory().getMatrix()) {
            if (ingredient != null && ingredient.getAmount() > 0) {
                maxCraftable = Math.min(maxCraftable, ingredient.getAmount());
            }
        }

        return maxCraftable * result.getAmount();
    }

    @EventHandler
    public void onItemPickup(org.bukkit.event.entity.EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();

        String itemType = event.getItem().getItemStack().getType().name();
        int amount = event.getItem().getItemStack().getAmount();

        plugin.getQuestManager().updateQuestProgress(player, QuestObjective.ObjectiveType.COLLECT_ITEM, itemType,
                amount);
    }
}
