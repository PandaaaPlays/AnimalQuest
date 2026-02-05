package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public abstract class Spell {
    private final String id;
    private final String name;
    private final double manaCost;
    private final int cooldownSeconds;

    public Spell(String id, String name, double manaCost, int cooldownSeconds) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
        this.cooldownSeconds = cooldownSeconds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getManaCost() {
        return manaCost;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public abstract void cast(Player player);

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(List.of(
                    Utils.applyFormat("&7Mana Cost: §b" + manaCost),
                    Utils.applyFormat("§7Cooldown: §e" + cooldownSeconds + "s"),
                    "",
                    Utils.applyFormat("§eRight-click to cast!")));

            NamespacedKey key = new NamespacedKey(AnimalQuest.getPlugin(), "spell");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);

            item.setItemMeta(meta);
        }
        return item;
    }
}
