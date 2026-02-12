package ca.pandaaa.animalquest.shop;

import ca.pandaaa.animalquest.items.AnimalQuestItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {
    private final AnimalQuestItem item;
    private final int amount;
    private final double priceMoney;
    private final List<ItemStack> priceItems;
    private final int minimumLevel;
    private final boolean broadcast;

    public ShopItem(AnimalQuestItem item, int amount, double priceMoney, List<ItemStack> priceItems,
            boolean broadcast) {
        this(item, amount, priceMoney, priceItems, 1, broadcast);
    }

    public ShopItem(AnimalQuestItem item, int amount, double priceMoney, List<ItemStack> priceItems, int minimumLevel,
            boolean broadcast) {
        this.item = item;
        this.amount = amount;
        this.priceMoney = priceMoney;
        this.priceItems = priceItems != null ? priceItems : new ArrayList<>();
        this.minimumLevel = minimumLevel;
        this.broadcast = broadcast;
    }

    public AnimalQuestItem getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public double getPriceMoney() {
        return priceMoney;
    }

    public List<ItemStack> getPriceItems() {
        return priceItems;
    }

    public int getMinimumLevel() {
        return minimumLevel;
    }

    public boolean shouldBroadcast() {
        return broadcast;
    }
}
