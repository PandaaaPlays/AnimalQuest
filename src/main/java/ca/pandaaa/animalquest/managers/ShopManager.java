package ca.pandaaa.animalquest.managers;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.guis.MountShopGUI;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.entity.EntityType.VILLAGER;
import static org.bukkit.persistence.PersistentDataType.STRING;

public class ShopManager implements Listener {
        private final Map<String, Shop> shops;
        private final NamespacedKey shopKey;
        private final AnimalQuest plugin;

        public ShopManager(AnimalQuest plugin) {
                this.shops = new HashMap<>();
                this.shopKey = new NamespacedKey(plugin, "shop");
                this.plugin = plugin;
                initShops();

                Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        private void initShops() {
                createSwordShop();
                createArmorShop();
                createMagicShop();
                createBowShop();
                createFishermanShop();
                createButcherShop();
                createSpellsShop();
        }

        private void createSwordShop() {
                Shop shop = new Shop("&b&lSword Shop");

                shop.addItem(new ShopItem(AnimalQuestItem.NOVICE_SWORD, 1, 100.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.APPRENTICE_SWORD, 1, 100.0,
                                List.of(AnimalQuestItem.NOVICE_SWORD.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_SWORD, 1, 150.0,
                                List.of(AnimalQuestItem.APPRENTICE_SWORD.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(5)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_BLADE, 1, 200.0,
                                List.of(AnimalQuestItem.ADVENTURER_SWORD.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(8),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.STEEL_GREATSWORD, 1, 300.0,
                                List.of(AnimalQuestItem.HUNTER_BLADE.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(10),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.HEROIC_BLADE, 1, 500.0,
                                List.of(AnimalQuestItem.STEEL_GREATSWORD.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(5),
                                                AnimalQuestItem.SUNSTONE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.GALILEO, 1, 750.0,
                                List.of(AnimalQuestItem.HEROIC_BLADE.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(10)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LACERATOR, 1, 1000.0,
                                List.of(AnimalQuestItem.GALILEO.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(8),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.AKIHATSU, 1, 1500.0,
                                List.of(AnimalQuestItem.LACERATOR.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(10),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SAKABATO, 1, 2500.0,
                                List.of(AnimalQuestItem.AKIHATSU.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(15),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(3)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.KUNITSUNA, 1, 3500.0,
                                List.of(AnimalQuestItem.SAKABATO.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(20),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(5)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.EXODIA, 1, 5000.0,
                                List.of(AnimalQuestItem.KUNITSUNA.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(25),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(20)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ZANGETSU, 1, 7500.0,
                                List.of(AnimalQuestItem.EXODIA.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(32),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(10)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ASTRALITH, 1, 10000.0,
                                List.of(AnimalQuestItem.ZANGETSU.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(20),
                                                AnimalQuestItem.VOID_CORE.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.THUAN, 1, 15000.0,
                                List.of(AnimalQuestItem.ASTRALITH.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(32),
                                                AnimalQuestItem.VOID_CORE.getItemStack(3)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ZENTETSUKEN, 1, 20000.0,
                                List.of(AnimalQuestItem.THUAN.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(40),
                                                AnimalQuestItem.VOID_CORE.getItemStack(5)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SHIZUMARU, 1, 30000.0,
                                List.of(AnimalQuestItem.ZENTETSUKEN.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(50),
                                                AnimalQuestItem.VOID_CORE.getItemStack(8)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BUDOKARA, 1, 40000.0,
                                List.of(AnimalQuestItem.SHIZUMARU.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(12),
                                                AnimalQuestItem.SUNSTONE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.AETHERIUM, 1, 55000.0,
                                List.of(AnimalQuestItem.BUDOKARA.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(15),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_BREAKER, 1, 75000.0,
                                List.of(AnimalQuestItem.AETHERIUM.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(20),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ATARAXIA, 1, 100000.0,
                                List.of(AnimalQuestItem.PRISMATIC_BREAKER.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(25),
                                                AnimalQuestItem.SUNSTONE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BRIGHTROAR, 1, 150000.0,
                                List.of(AnimalQuestItem.ATARAXIA.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(30),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.MURASAME, 1, 250000.0,
                                List.of(AnimalQuestItem.BRIGHTROAR.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(35),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ELUCIDATOR, 1, 400000.0,
                                List.of(AnimalQuestItem.MURASAME.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(40),
                                                AnimalQuestItem.SUNSTONE.getItemStack(64),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ASHURA, 1, 600000.0,
                                List.of(AnimalQuestItem.ELUCIDATOR.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(45),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.NIGHTS_EDGE, 1, 900000.0,
                                List.of(AnimalQuestItem.ASHURA.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(50),
                                                AnimalQuestItem.SUNSTONE.getItemStack(64),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.DARK_REPULSOR, 1, 1500000.0,
                                List.of(AnimalQuestItem.NIGHTS_EDGE.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(55),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ASHBRINGER, 1, 2500000.0,
                                List.of(AnimalQuestItem.DARK_REPULSOR.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(60),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(2),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LUXURIA, 1, 4000000.0,
                                List.of(AnimalQuestItem.ASHBRINGER.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(64),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(4),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ASTRAL_BLADE, 1, 7500000.0,
                                List.of(AnimalQuestItem.LUXURIA.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(64),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(8),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.TYRFING, 1, 15000000.0,
                                List.of(AnimalQuestItem.ASTRAL_BLADE.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(64),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(32)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.KURIKARA, 1, 25000000.0,
                                List.of(AnimalQuestItem.TYRFING.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(64),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(2),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(64),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(64)),
                                true));

                shops.put("sword", shop);
        }

        private void createArmorShop() {
                Shop shop = new Shop("&3&lArmor Shop");

                // Helmets
                shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_HELMET, 1, 50.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_HELMET, 1, 100.0,
                                List.of(AnimalQuestItem.ADVENTURER_HELMET.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(2)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_HELMET, 1, 250.0,
                                List.of(AnimalQuestItem.HUNTER_HELMET.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(5),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_HELMET, 1, 600.0,
                                List.of(AnimalQuestItem.OLYMPIAN_HELMET.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(3),
                                                AnimalQuestItem.SUNSTONE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_HELMET, 1, 1500.0,
                                List.of(AnimalQuestItem.SLAYER_HELMET.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_HELMET, 1, 4000.0,
                                List.of(AnimalQuestItem.TITANIUM_HELMET.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(3),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_HELMET, 1, 12000.0,
                                List.of(AnimalQuestItem.PRISMATIC_HELMET.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(8),
                                                AnimalQuestItem.VOID_CORE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_HELMET, 1, 35000.0,
                                List.of(AnimalQuestItem.LUNAR_HELMET.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(5),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(2),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(10)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_HELMET, 1, 100000.0,
                                List.of(AnimalQuestItem.MOLTEN_HELMET.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(10),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(15),
                                                AnimalQuestItem.SUNSTONE.getItemStack(25)),
                                true));

                // Chestplates
                shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_CHESTPLATE, 1, 80.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_CHESTPLATE, 1, 160.0,
                                List.of(AnimalQuestItem.ADVENTURER_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(4)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_CHESTPLATE, 1, 400.0,
                                List.of(AnimalQuestItem.HUNTER_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(8),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_CHESTPLATE, 1, 1000.0,
                                List.of(AnimalQuestItem.OLYMPIAN_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(5),
                                                AnimalQuestItem.SUNSTONE.getItemStack(4)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_CHESTPLATE, 1, 2500.0,
                                List.of(AnimalQuestItem.SLAYER_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(10),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_CHESTPLATE, 1, 6500.0,
                                List.of(AnimalQuestItem.TITANIUM_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(5),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(4)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_CHESTPLATE, 1, 20000.0,
                                List.of(AnimalQuestItem.PRISMATIC_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(12),
                                                AnimalQuestItem.VOID_CORE.getItemStack(4)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_CHESTPLATE, 1, 60000.0,
                                List.of(AnimalQuestItem.LUNAR_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(10),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(4),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(15)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_CHESTPLATE, 1, 160000.0,
                                List.of(AnimalQuestItem.MOLTEN_CHESTPLATE.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(20),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(2),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(25),
                                                AnimalQuestItem.SUNSTONE.getItemStack(40)),
                                true));

                // Leggings
                shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_LEGGINGS, 1, 70.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_LEGGINGS, 1, 140.0,
                                List.of(AnimalQuestItem.ADVENTURER_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_LEGGINGS, 1, 350.0,
                                List.of(AnimalQuestItem.HUNTER_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(6),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_LEGGINGS, 1, 850.0,
                                List.of(AnimalQuestItem.OLYMPIAN_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(4),
                                                AnimalQuestItem.SUNSTONE.getItemStack(3)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_LEGGINGS, 1, 2100.0,
                                List.of(AnimalQuestItem.SLAYER_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(8),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_LEGGINGS, 1, 5500.0,
                                List.of(AnimalQuestItem.TITANIUM_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(4)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_LEGGINGS, 1, 17000.0,
                                List.of(AnimalQuestItem.PRISMATIC_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(10),
                                                AnimalQuestItem.VOID_CORE.getItemStack(3)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_LEGGINGS, 1, 50000.0,
                                List.of(AnimalQuestItem.LUNAR_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(8),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(3),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(12)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_LEGGINGS, 1, 140000.0,
                                List.of(AnimalQuestItem.MOLTEN_LEGGINGS.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(15),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(20),
                                                AnimalQuestItem.SUNSTONE.getItemStack(32)),
                                true));

                // Boots
                shop.addItem(new ShopItem(AnimalQuestItem.ADVENTURER_BOOTS, 1, 40.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.HUNTER_BOOTS, 1, 80.0,
                                List.of(AnimalQuestItem.ADVENTURER_BOOTS.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(2)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.OLYMPIAN_BOOTS, 1, 200.0,
                                List.of(AnimalQuestItem.HUNTER_BOOTS.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(4),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.SLAYER_BOOTS, 1, 500.0,
                                List.of(AnimalQuestItem.OLYMPIAN_BOOTS.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(2),
                                                AnimalQuestItem.SUNSTONE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.TITANIUM_BOOTS, 1, 1200.0,
                                List.of(AnimalQuestItem.SLAYER_BOOTS.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.PRISMATIC_BOOTS, 1, 3200.0,
                                List.of(AnimalQuestItem.TITANIUM_BOOTS.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(2),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.LUNAR_BOOTS, 1, 9500.0,
                                List.of(AnimalQuestItem.PRISMATIC_BOOTS.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(5),
                                                AnimalQuestItem.VOID_CORE.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.MOLTEN_BOOTS, 1, 28000.0,
                                List.of(AnimalQuestItem.LUNAR_BOOTS.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(5),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(1),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(8)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.DARK_MATTER_BOOTS, 1, 80000.0,
                                List.of(AnimalQuestItem.MOLTEN_BOOTS.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(10),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(12),
                                                AnimalQuestItem.SUNSTONE.getItemStack(20)),
                                true));

                shops.put("armor", shop);
        }

        private void createMagicShop() {
                Shop shop = new Shop("&d&lMagic Shop");

                shop.addItem(new ShopItem(AnimalQuestItem.MANA_BOTTLE, 1, 500.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(1)), false));
                shop.addItem(new ShopItem(AnimalQuestItem.MANA_BOTTLE, 16, 8000.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(12),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(2)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.MANA_BOTTLE, 64, 32000.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(40),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(10),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.HEALING_POTION_L1, 1, 150.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(2)), false));
                shop.addItem(new ShopItem(AnimalQuestItem.HEALING_POTION_L2, 1, 400.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(5),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.HERBAL_BREW, 1, 300.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(8),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.RESISTANCE_ELIXIR, 1, 750.0,
                                List.of(AnimalQuestItem.ANCIENT_RUNE.getItemStack(5),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.REGENERATION_FLASK, 1, 1500.0,
                                List.of(AnimalQuestItem.SUNSTONE.getItemStack(10),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(2)),
                                true));

                shops.put("magic", shop);
        }

        private void createBowShop() {
                Shop shop = new Shop("&e&lBow Shop");
                shop.addItem(new ShopItem(AnimalQuestItem.BOW, 1, 100.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_2, 1, 200.0,
                                List.of(AnimalQuestItem.BOW.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_3, 1, 400.0,
                                List.of(AnimalQuestItem.BOW_2.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(5),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(1)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_4, 1, 800.0,
                                List.of(AnimalQuestItem.BOW_3.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(8),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(3),
                                                AnimalQuestItem.SUNSTONE.getItemStack(2)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_5, 1, 1500.0,
                                List.of(AnimalQuestItem.BOW_4.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(5),
                                                AnimalQuestItem.SUNSTONE.getItemStack(5),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_6, 1, 3000.0,
                                List.of(AnimalQuestItem.BOW_5.getItemStack(1),
                                                AnimalQuestItem.SUNSTONE.getItemStack(8),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(3),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_7, 1, 7500.0,
                                List.of(AnimalQuestItem.BOW_6.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(8),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(5),
                                                AnimalQuestItem.VOID_CORE.getItemStack(1)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_8, 1, 15000.0,
                                List.of(AnimalQuestItem.BOW_7.getItemStack(1),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(10),
                                                AnimalQuestItem.CELESTIAL_SHARD.getItemStack(2),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(10)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.BOW_9, 1, 50000.0,
                                List.of(AnimalQuestItem.BOW_8.getItemStack(1),
                                                AnimalQuestItem.VOID_CORE.getItemStack(20),
                                                AnimalQuestItem.ETERNAL_EMBLEM.getItemStack(2),
                                                AnimalQuestItem.SUNSTONE.getItemStack(32),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(16)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.ARROW, 64, 64.0, null, false));

                shops.put("bow", shop);
        }

        private void createFishermanShop() {
                Shop shop = new Shop("&b&lFisherman Shop");

                shop.addItem(new ShopItem(AnimalQuestItem.BASIC_FISHING_ROD, 1, 50.0, null, false));
                shop.addItem(new ShopItem(AnimalQuestItem.APPRENTICE_FISHING_ROD, 1, 150.0,
                                List.of(AnimalQuestItem.BASIC_FISHING_ROD.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(3)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.MASTER_FISHING_ROD, 1, 400.0,
                                List.of(AnimalQuestItem.APPRENTICE_FISHING_ROD.getItemStack(1),
                                                AnimalQuestItem.WILD_ESSENCE.getItemStack(5),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(2)),
                                false));
                shop.addItem(new ShopItem(AnimalQuestItem.POSEIDON_FISHING_ROD, 1, 1500.0,
                                List.of(AnimalQuestItem.MASTER_FISHING_ROD.getItemStack(1),
                                                AnimalQuestItem.ANCIENT_RUNE.getItemStack(5),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(1),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(2)),
                                true));
                shop.addItem(new ShopItem(AnimalQuestItem.GREAT_SEA_FISHING_ROD, 1, 6000.0,
                                List.of(AnimalQuestItem.POSEIDON_FISHING_ROD.getItemStack(1),
                                                AnimalQuestItem.MYSTIC_DIAMOND.getItemStack(5),
                                                AnimalQuestItem.ABYSSAL_CRYSTAL.getItemStack(10)),
                                true));

                shops.put("fisherman", shop);
        }

        private void createButcherShop() {
                Shop shop = new Shop("&c&lButcher Shop");

                shop.addItem(new ShopItem(AnimalQuestItem.STEAK, 1, 50.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(2)), false));
                shop.addItem(new ShopItem(AnimalQuestItem.CHICKEN, 1, 50.0,
                                List.of(AnimalQuestItem.WILD_ESSENCE.getItemStack(2)), false));
                shop.addItem(new ShopItem(AnimalQuestItem.GOLDEN_CARROT, 1, 100.0,
                                List.of(AnimalQuestItem.ANCIENT_RUNE.getItemStack(2)), true));

                shops.put("butcher", shop);
        }

        private void createSpellsShop() {
                Shop shop = new Shop("&5&lSpells Shop");

                Map<String, Double> moneyPrices = new HashMap<>();
                Map<String, Integer> sapphirePrices = new HashMap<>();

                // Unique Basic Prices
                moneyPrices.put("fireball", 500.0);
                sapphirePrices.put("fireball", 2);

                moneyPrices.put("stone_shield", 2500.0);
                sapphirePrices.put("stone_shield", 3);

                // Unique Medium Prices
                moneyPrices.put("strength", 5000.0);
                sapphirePrices.put("strength", 5);

                moneyPrices.put("neptunes_blessing", 5000.0);
                sapphirePrices.put("neptunes_blessing", 5);

                moneyPrices.put("fire_spirit", 10000.0);
                sapphirePrices.put("fire_spirit", 10);

                moneyPrices.put("healing_spree", 10000.0);
                sapphirePrices.put("healing_spree", 10);

                moneyPrices.put("aero_glide", 10000.0);
                sapphirePrices.put("aero_glide", 10);

                moneyPrices.put("flower_shield", 10000.0);
                sapphirePrices.put("flower_shield", 10);

                moneyPrices.put("fire_shield", 10000.0);
                sapphirePrices.put("fire_shield", 10);

                moneyPrices.put("sonic_boom", 10000.0);
                sapphirePrices.put("sonic_boom", 10);

                moneyPrices.put("gravity_pull", 10000.0);
                sapphirePrices.put("gravity_pull", 10);

                moneyPrices.put("endurance", 15000.0);
                sapphirePrices.put("endurance", 15);

                moneyPrices.put("lightning_speed", 20000.0);
                sapphirePrices.put("lightning_speed", 20);

                // Unique Advanced Prices
                moneyPrices.put("abyssal_anchor", 30000.0);
                sapphirePrices.put("abyssal_anchor", 20);

                moneyPrices.put("craftsmans_anvil", 30000.0);
                sapphirePrices.put("craftsmans_anvil", 25);

                moneyPrices.put("cyclone", 35000.0);
                sapphirePrices.put("cyclone", 25);

                moneyPrices.put("charge", 50000.0);
                sapphirePrices.put("charge", 30);

                moneyPrices.put("immortal", 50000.0);
                sapphirePrices.put("immortal", 30);

                moneyPrices.put("vampiric_touch", 50000.0);
                sapphirePrices.put("vampiric_touch", 30);

                moneyPrices.put("static_discharge", 50000.0);
                sapphirePrices.put("static_discharge", 30);

                moneyPrices.put("dragons_strike", 75000.0);
                sapphirePrices.put("dragons_strike", 40);

                moneyPrices.put("meteor_rain", 75000.0);
                sapphirePrices.put("meteor_rain", 40);

                moneyPrices.put("phoenix_rebirth", 100000.0);
                sapphirePrices.put("phoenix_rebirth", 50);

                if (plugin.getSpellManager() != null) {
                        List<ShopItem> spellItems = new ArrayList<>();
                        for (ca.pandaaa.animalquest.spells.Spell spell : plugin.getSpellManager().getRegisteredSpells()
                                        .values()) {
                                String id = spell.getId().toLowerCase();
                                double money = moneyPrices.getOrDefault(id, 10000000.0);
                                int sapphires = sapphirePrices.getOrDefault(id, 10000);

                                spellItems.add(new ShopItem(spell.getItem(), 1, money,
                                                List.of(AnimalQuestItem.SAPHIRE.getItemStack(sapphires)),
                                                money >= 10000.0));
                        }

                        spellItems.sort(Comparator.comparingDouble(ShopItem::getPriceMoney));
                        for (ShopItem item : spellItems) {
                                shop.addItem(item);
                        }
                }

                shops.put("spells", shop);
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
                                                Utils.getAnimalQuestName() + " &7&l>> &bShop NPC removed."));
                                return;
                        }
                }
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must be looking at a shop NPC to remove it."));
        }

        public void openShop(Player player, String shopName) {
                if (shopName.equalsIgnoreCase("mount")) {
                        new MountShopGUI(player).open();
                        return;
                }
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
