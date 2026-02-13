package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.ShopGUI;
import ca.pandaaa.animalquest.enums.AnimalQuestItem;
import ca.pandaaa.animalquest.shop.Shop;
import ca.pandaaa.animalquest.shop.ShopItem;
import ca.pandaaa.animalquest.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.entity.EntityType.VILLAGER;
import static org.bukkit.persistence.PersistentDataType.STRING;

public class ShopManager implements Listener {
    private final Map<String, Shop> shops;
    private final NamespacedKey shopKey;

    public ShopManager(AnimalQuest plugin) {
        this.shops = new HashMap<>();
        this.shopKey = new NamespacedKey(plugin, "shop");
        initShops();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initShops() {
        createSwordShop();
        createArmorShop();
        createExplorerShop();
        createMagicShop();
    }

    private void createSwordShop() {
        Shop shop = new Shop("&b&lSword Shop");

        shop.addItem(new ShopItem(AnimalQuestItem.NOVICE_SWORD, 1, 100.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.APPRENTICE_SWORD, 1, 100.0,
            List.of(AnimalQuestItem.NOVICE_SWORD.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_SWORD, 1, 150.0,
            List.of(AnimalQuestItem.APPRENTICE_SWORD.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_BLADE, 1, 200.0,
            List.of(AnimalQuestItem.ADVENTURER_SWORD.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.STEEL_GREATSWORD, 1, 300.0,
            List.of(AnimalQuestItem.HUNTER_BLADE.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.HEROIC_BLADE, 1, 500.0,
            List.of(AnimalQuestItem.STEEL_GREATSWORD.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.GALILEO, 1, 750.0,
            List.of(AnimalQuestItem.HEROIC_BLADE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LACERATOR, 1, 1000.0,
            List.of(AnimalQuestItem.GALILEO.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.AKIHATSU, 1, 1500.0,
            List.of(AnimalQuestItem.LACERATOR.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.SAKABATO, 1, 2500.0,
            List.of(AnimalQuestItem.AKIHATSU.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.KUNITSUNA, 1, 3500.0,
            List.of(AnimalQuestItem.SAKABATO.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.EXODIA, 1, 5000.0,
            List.of(AnimalQuestItem.KUNITSUNA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ZANGETSU, 1, 7500.0,
            List.of(AnimalQuestItem.EXODIA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ASTRALITH, 1, 10000.0,
            List.of(AnimalQuestItem.ZANGETSU.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.THUAN, 1, 15000.0,
            List.of(AnimalQuestItem.ASTRALITH.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ZENTETSUKEN, 1, 20000.0,
            List.of(AnimalQuestItem.THUAN.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.SHIZUMARU, 1, 30000.0,
            List.of(AnimalQuestItem.ZENTETSUKEN.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.BUDOKARA, 1, 40000.0,
            List.of(AnimalQuestItem.SHIZUMARU.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.AETHERIUM, 1, 55000.0,
            List.of(AnimalQuestItem.BUDOKARA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_BREAKER, 1, 75000.0,
            List.of(AnimalQuestItem.AETHERIUM.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ATARAXIA, 1, 100000.0,
            List.of(AnimalQuestItem.PRISMATIC_BREAKER.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.BRIGHTROAR, 1, 150000.0,
            List.of(AnimalQuestItem.ATARAXIA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.MURASAME, 1, 250000.0,
            List.of(AnimalQuestItem.BRIGHTROAR.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ELUCIDATOR, 1, 400000.0,
            List.of(AnimalQuestItem.MURASAME.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ASHURA, 1, 600000.0,
            List.of(AnimalQuestItem.ELUCIDATOR.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.NIGHTS_EDGE, 1, 900000.0,
            List.of(AnimalQuestItem.ASHURA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.DARK_REPULSOR, 1, 1500000.0,
            List.of(AnimalQuestItem.NIGHTS_EDGE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.ASHBRINGER, 1, 2500000.0,
            List.of(AnimalQuestItem.DARK_REPULSOR.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LUXURIA, 1, 4000000.0,
            List.of(AnimalQuestItem.ASHBRINGER.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.ASTRAL_BLADE, 1, 7500000.0,
            List.of(AnimalQuestItem.LUXURIA.getItemStack(1)), true));
        shop.addItem(new ShopItem(AnimalQuestItem.TYRFING, 1, 15000000.0,
            List.of(AnimalQuestItem.ASTRAL_BLADE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.KURIKARA, 1, 25000000.0,
            List.of(AnimalQuestItem.TYRFING.getItemStack(1)), true));

        shops.put("sword", shop);
    }

    private void createArmorShop() {
        Shop shop = new Shop("&b&lArmor Shop");

        // Helmets
        shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_HELMET, 1, 50.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_HELMET, 1, 100.0,
            List.of(AnimalQuestItem.ADVENTURER_HELMET.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_HELMET, 1, 250.0,
            List.of(AnimalQuestItem.HUNTER_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_HELMET, 1, 600.0,
            List.of(AnimalQuestItem.OLYMPIAN_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_HELMET, 1, 1500.0,
            List.of(AnimalQuestItem.SLAYER_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_HELMET, 1, 4000.0,
            List.of(AnimalQuestItem.TITANIUM_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_HELMET, 1, 12000.0,
            List.of(AnimalQuestItem.PRISMATIC_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_HELMET, 1, 35000.0,
            List.of(AnimalQuestItem.LUNAR_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_HELMET, 1, 100000.0,
            List.of(AnimalQuestItem.MOLTEN_HELMET.getItemStack(1)),
            true));

        // Chestplates
        shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_CHESTPLATE, 1, 80.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_CHESTPLATE, 1, 160.0,
            List.of(AnimalQuestItem.ADVENTURER_CHESTPLATE.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_CHESTPLATE, 1, 400.0,
            List.of(AnimalQuestItem.HUNTER_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_CHESTPLATE, 1, 1000.0,
            List.of(AnimalQuestItem.OLYMPIAN_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_CHESTPLATE, 1, 2500.0,
            List.of(AnimalQuestItem.SLAYER_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_CHESTPLATE, 1, 6500.0,
            List.of(AnimalQuestItem.TITANIUM_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_CHESTPLATE, 1, 20000.0,
            List.of(AnimalQuestItem.PRISMATIC_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_CHESTPLATE, 1, 60000.0,
            List.of(AnimalQuestItem.LUNAR_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_CHESTPLATE, 1, 160000.0,
            List.of(AnimalQuestItem.MOLTEN_CHESTPLATE.getItemStack(1)),
            true));

        // Leggings
        shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_LEGGINGS, 1, 70.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_LEGGINGS, 1, 140.0,
            List.of(AnimalQuestItem.ADVENTURER_LEGGINGS.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_LEGGINGS, 1, 350.0,
            List.of(AnimalQuestItem.HUNTER_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_LEGGINGS, 1, 850.0,
            List.of(AnimalQuestItem.OLYMPIAN_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_LEGGINGS, 1, 2100.0,
            List.of(AnimalQuestItem.SLAYER_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_LEGGINGS, 1, 5500.0,
            List.of(AnimalQuestItem.TITANIUM_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_LEGGINGS, 1, 17000.0,
            List.of(AnimalQuestItem.PRISMATIC_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_LEGGINGS, 1, 50000.0,
            List.of(AnimalQuestItem.LUNAR_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_LEGGINGS, 1, 140000.0,
            List.of(AnimalQuestItem.MOLTEN_LEGGINGS.getItemStack(1)),
            true));

        // Boots
        shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_BOOTS, 1, 40.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_BOOTS, 1, 80.0,
            List.of(AnimalQuestItem.ADVENTURER_BOOTS.getItemStack(1)),
            false));
        shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_BOOTS, 1, 200.0,
            List.of(AnimalQuestItem.HUNTER_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_BOOTS, 1, 500.0,
            List.of(AnimalQuestItem.OLYMPIAN_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_BOOTS, 1, 1200.0,
            List.of(AnimalQuestItem.SLAYER_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_BOOTS, 1, 3200.0,
            List.of(AnimalQuestItem.TITANIUM_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_BOOTS, 1, 9500.0,
            List.of(AnimalQuestItem.PRISMATIC_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_BOOTS, 1, 28000.0,
            List.of(AnimalQuestItem.LUNAR_BOOTS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_BOOTS, 1, 80000.0,
            List.of(AnimalQuestItem.MOLTEN_BOOTS.getItemStack(1)),
            true));

        shops.put("armor", shop);
    }

    private void createExplorerShop() {
        Shop shop = new Shop("&a&lExplorer Shop");

        shop.addItem(new ShopItem(AnimalQuestItem.EXPLORER_SWORD, 1, 50.0, null, false));
        shop.addItem(new ShopItem(AnimalQuestItem.EXPLORER_HELMET, 1, 25.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.EXPLORER_CHESTPLATE, 1, 40.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.EXPLORER_LEGGINGS, 1, 35.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.EXPLORER_BOOTS, 1, 20.0, null, true));

        shop.addItem(new ShopItem(AnimalQuestItem.GUIDE_SWORD, 1, 150.0,
            List.of(AnimalQuestItem.EXPLORER_SWORD.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.GUIDE_HELMET, 1, 75.0,
            List.of(AnimalQuestItem.EXPLORER_HELMET.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.GUIDE_CHESTPLATE, 1, 120.0,
            List.of(AnimalQuestItem.EXPLORER_CHESTPLATE.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.GUIDE_LEGGINGS, 1, 105.0,
            List.of(AnimalQuestItem.EXPLORER_LEGGINGS.getItemStack(1)),
            true));
        shop.addItem(new ShopItem(AnimalQuestItem.GUIDE_BOOTS, 1, 60.0,
            List.of(AnimalQuestItem.EXPLORER_BOOTS.getItemStack(1)),
            true));

        shops.put("explorer", shop);
    }

    private void createMagicShop() {
        Shop shop = new Shop("&d&lMagic Shop");

        shop.addItem(new ShopItem(AnimalQuestItem.MANA_BOTTLE, 1, 500.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.HEALING_POTION_L1, 1, 150.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.HEALING_POTION_L2, 1, 400.0, null, true));
        shop.addItem(new ShopItem(AnimalQuestItem.ARROW, 64, 64.0, null, true));

        shops.put("magic", shop);
    }

    public void summonShopNPC(Player player, String shopName) {
        Shop shop = getShop(shopName);
        if (shop != null) {
            Villager npc = (Villager) player.getWorld().spawnEntity(player.getLocation(), VILLAGER);
            npc.setCustomName(Utils.applyFormat(shop.getName()));
            npc.setCustomNameVisible(true);
            npc.setAI(false);
            npc.setInvulnerable(true);
            npc.setSilent(true);
            npc.setProfession(Villager.Profession.NITWIT);
            npc.getPersistentDataContainer().set(shopKey, STRING, shopName);
            player.sendMessage(Utils.applyFormat("&aShop NPC summoned!"));
        }
    }

    public void removeShopNpc(Player player) {
        org.bukkit.util.RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(),
            player.getEyeLocation().getDirection(), 5, entity -> entity != player);

        if (result != null && result.getHitEntity() != null) {
            Entity target = result.getHitEntity();
            if (target.getPersistentDataContainer().has(shopKey,
                STRING)) {
                target.remove();
                player.sendMessage(Utils.applyFormat(
                    Utils.getAnimalQuestName() + " &8&l>> &bShop NPC removed."));
                return;
            }
        }
        player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must be looking at a shop NPC to remove it."));
    }

    public void openShop(Player player, String shopName) {
        Shop shop = getShop(shopName);
        if (shop != null) {
            new ShopGUI(shop).open(player);
        }
    }

    public Shop getShop(String name) {
        return shops.get(name.toLowerCase());
    }

    public Map<String, Shop> getShops() {
        return shops;
    }

    @EventHandler
    public void onNpcClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getPersistentDataContainer().has(shopKey, STRING)) {
            event.setCancelled(true);
            String shopName = event.getRightClicked().getPersistentDataContainer().get(shopKey, STRING);
            openShop(event.getPlayer(), shopName);
        }
    }
}
