package ca.pandaaa.animalquest.utils;

import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomUnbreaking {
    public void damageItem(ItemStack item) {
        int level = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (itemDurabilityDecreaseRandom(itemDurabilityDecreaseChance(level))) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable damageable) {
                damageable.setDamage(damageable.getDamage() + 1);
                item.setItemMeta(meta);
            }
        }
    }

    /*
     * Formula breakdown:
     * - Levels 1-4: 60 + 40/(level+1)
     * → Level 1: 80%, Level 2: 73.3%, Level 3: 70%, Level 4: 68%
     * - Level 5: 66.5% (fixed)
     * - Levels 6-21: 65 - (level-6)
     * → Decreases by 1% per level from 65% to 50%
     * - Levels 22-61: 50 - (level-21)/2
     * → Decreases by 0.5% per level from 50% to 30%
     * - Levels 62-91: 30 - (level-61)/3
     * → Decreases by ~0.33% per level from 30% to 20%
     * - Levels 92-291: 20 - (level-91)/10
     * → Decreases by 0.1% per level from 20% to 0%
     * - Level 292+: 0% (item never takes damage)
     */
    private double itemDurabilityDecreaseChance(int level) {
        double chance = 100D;
        if (level <= 4)
            chance = 60 + 40 / (level + 1);
        else if (level == 5)
            chance = 66.5D;
        else if (level <= 21)
            chance = 65D - (level - 6D);
        else if (level <= 61)
            chance = 50D - ((level - 21D) / 2D);
        else if (level <= 91)
            chance = 30D - ((level - 61D) / 3D);
        else if (level <= 291)
            chance = 20D - ((level - 91D) / 10D);
        else
            chance = 0;
        return chance;
    }

    private boolean itemDurabilityDecreaseRandom(double number) {
        Random random = new Random();
        double randomDouble = random.nextDouble();
        return randomDouble * 100D <= number;
    }

}
