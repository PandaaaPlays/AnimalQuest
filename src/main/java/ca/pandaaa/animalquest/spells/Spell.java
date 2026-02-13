package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.utils.CustomHead;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Spell {
    private final String id;
    private final String name;
    private final int manaCost;
    private final int cooldownSeconds;
    private final String description;
    private final String texture;

    public Spell(String id, String name, int manaCost, int cooldownSeconds, String description, String texture) {
        this.id = id;
        this.name = name;
        this.manaCost = manaCost;
        this.cooldownSeconds = cooldownSeconds;
        this.description = description;
        this.texture = texture;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public String getDescription() {
        return description;
    }

    public void sendActivationMessage(Player player) {
        player.sendMessage(Utils.applyFormat("&5[&d" + name + "&5][&d-" + manaCost + "&d Mana&5]"));
    }

    public abstract void cast(Player player);

    private List<String> wordWrap(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // If adding this word would exceed maxWidth, start a new line
            if (currentLine.length() + word.length() + 1 > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                currentLine.append(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        // Add the last line if there's anything left
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public ItemStack getItem() {
        ItemStack item = CustomHead.createHead(texture);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Utils.applyFormat("&5&l" + name));

            List<String> lore = new ArrayList<>();
            lore.add(Utils.applyFormat("&bCost: &f" + manaCost + " Mana"));
            lore.add(Utils.applyFormat("&bCooldown: &f" + cooldownSeconds + " sec"));
            lore.add("");
            lore.add(Utils.applyFormat("&d&lDescription"));
            List<String> wrappedDescription = wordWrap(description, 35);
            for (String line : wrappedDescription) {
                lore.add(Utils.applyFormat("&7" + line));
            }

            meta.setLore(lore);

            NamespacedKey key = new NamespacedKey(ca.pandaaa.animalquest.AnimalQuest.getPlugin(), "spell");
            meta.getPersistentDataContainer().set(key, org.bukkit.persistence.PersistentDataType.STRING, id);

            item.setItemMeta(meta);
        }
        return item;
    }
}
