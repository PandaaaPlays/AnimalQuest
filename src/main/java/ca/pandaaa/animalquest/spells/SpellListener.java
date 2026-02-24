package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.managers.SpellManager;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.entity.Fireball;
import org.bukkit.metadata.MetadataValue;
import java.util.List;

public class SpellListener implements Listener {
    private final SpellManager spellManager;
    private final NamespacedKey spellKey = new NamespacedKey(AnimalQuest.getPlugin(), "spell");

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

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball fireball) {
            if (fireball.hasMetadata("spell")) {
                List<MetadataValue> values = fireball.getMetadata("spell");
                for (MetadataValue value : values) {
                    if (value.asString().equals("fireball")) {
                        event.blockList().clear();
                        break;
                    }
                }
            }
        }
    }
}
