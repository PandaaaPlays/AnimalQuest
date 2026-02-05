package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpellListener implements Listener {
    private final SpellManager spellManager;
    private final NamespacedKey spellKey = new NamespacedKey(AnimalQuest.getPlugin(), "spell");;

    public SpellListener(SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta())
            return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        String spellId = meta.getPersistentDataContainer().get(spellKey, PersistentDataType.STRING);
        if (spellId == null)
            return;

        event.setCancelled(true);
        Spell spell = spellManager.getSpellById(spellId);
        if (spell != null) {
            spellManager.castSpell(event.getPlayer(), spell);
        }
    }
}
