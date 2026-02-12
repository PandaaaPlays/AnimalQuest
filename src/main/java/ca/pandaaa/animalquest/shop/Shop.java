package ca.pandaaa.animalquest.shop;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final String name;
    private final List<ShopItem> items;

    public Shop(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }
}
