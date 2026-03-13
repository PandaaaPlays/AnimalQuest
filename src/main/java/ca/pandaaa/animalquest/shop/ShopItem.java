package ca.pandaaa.animalquest.shop;

import ca.pandaaa.animalquest.enums.AnimalQuestItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {
    private final AnimalQuestItem item;
    private final ItemStack customItemStack;
    private final int amount;
    private final double priceMoney;
    private final List<ItemStack> priceItems;
    private final int minimumLevel;
    private final boolean broadcast;

    public ShopItem(AnimalQuestItem item, int amount, double priceMoney, List<ItemStack> priceItems,
            boolean broadcast) {
        this(item, null, amount, priceMoney, priceItems, 1, broadcast);
    }

    public ShopItem(ItemStack customItemStack, int amount, double priceMoney, List<ItemStack> priceItems,
            boolean broadcast) {
        this(null, customItemStack, amount, priceMoney, priceItems, 1, broadcast);
    }

    public ShopItem(AnimalQuestItem item, ItemStack customItemStack, int amount, double priceMoney,
            List<ItemStack> priceItems, int minimumLevel,
            boolean broadcast) {
        this.item = item;
        this.customItemStack = customItemStack;
        this.amount = amount;
        this.priceMoney = priceMoney;
        this.priceItems = priceItems != null ? priceItems : new ArrayList<>();
        this.minimumLevel = minimumLevel;
        this.broadcast = broadcast;
    }

    public AnimalQuestItem getItem() {
        return item;
    }

    public ItemStack getCustomItemStack() {
        return customItemStack;
    }

    public ItemStack getItemStack() {
        if (customItemStack != null) {
            ItemStack clone = customItemStack.clone();
            clone.setAmount(amount);
            return clone;
        }
        return item.getItemStack(amount);
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
