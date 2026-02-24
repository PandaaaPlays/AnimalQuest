package ca.pandaaa.animalquest.quests;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private final List<QuestStep> steps;
    private final QuestReward reward;
    private final Material iconMaterial;
    private final int requiredLevel;
    private final List<String> requiredQuests;

    public Quest(String id, String name, String description, List<QuestStep> steps,
            QuestReward reward, Material iconMaterial, int requiredLevel, List<String> requiredQuests) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.reward = reward;
        this.iconMaterial = iconMaterial;
        this.requiredLevel = requiredLevel;
        this.requiredQuests = requiredQuests != null ? requiredQuests : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<QuestStep> getSteps() {
        return steps;
    }

    public QuestStep getStep(int index) {
        if (index >= 0 && index < steps.size()) {
            return steps.get(index);
        }
        return null;
    }

    public int getTotalSteps() {
        return steps.size();
    }

    public QuestReward getReward() {
        return reward;
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public List<String> getRequiredQuests() {
        return requiredQuests;
    }

    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(iconMaterial);
        org.bukkit.inventory.meta.ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§l" + name);
            List<String> lore = new ArrayList<>();
            lore.add("§7" + description);
            lore.add("");
            lore.add("§eSteps: §f" + steps.size());
            lore.add("§eRequired Level: §f" + requiredLevel);
            meta.setLore(lore);
            icon.setItemMeta(meta);
        }
        return icon;
    }
}
