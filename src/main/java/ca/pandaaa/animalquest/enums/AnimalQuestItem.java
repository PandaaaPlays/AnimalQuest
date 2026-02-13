package ca.pandaaa.animalquest.enums;

import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public enum AnimalQuestItem {
    // Swords
    NOVICE_SWORD(Material.IRON_SWORD, "Novice Sword", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 1)),
    APPRENTICE_SWORD(Material.IRON_SWORD, "Apprentice Sword", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 1).enchant(Enchantment.SMITE, 1)),
    ADVENTURER_SWORD(Material.IRON_SWORD, "Adventurer Sword", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2).enchant(Enchantment.SMITE, 1)
            .enchant(Enchantment.SHARPNESS, 1)),
    HUNTER_BLADE(Material.IRON_SWORD, "Hunter Blade", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 2)
            .enchant(Enchantment.SHARPNESS, 1)),
    STEEL_GREATSWORD(Material.IRON_SWORD, "Steel Greatsword", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 2)
            .enchant(Enchantment.SHARPNESS, 2)),
    HEROIC_BLADE(Material.DIAMOND_SWORD, "Heroic Blade", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2).enchant(Enchantment.SMITE, 3)
            .enchant(Enchantment.SHARPNESS, 2)),
    GALILEO(Material.DIAMOND_SWORD, "Galileo", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2).enchant(Enchantment.SMITE, 3)
            .enchant(Enchantment.SHARPNESS, 3)),
    LACERATOR(Material.DIAMOND_SWORD, "Lacerator", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2).enchant(Enchantment.SMITE, 4)
            .enchant(Enchantment.SHARPNESS, 3)),
    AKIHATSU(Material.DIAMOND_SWORD, "Akihatsu", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 4)
            .enchant(Enchantment.SHARPNESS, 3)
            .enchant(Enchantment.FIRE_ASPECT, 1)),
    SAKABATO(Material.DIAMOND_SWORD, "Sakabato", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 4)
            .enchant(Enchantment.SHARPNESS, 4)
            .enchant(Enchantment.FIRE_ASPECT, 1)),
    KUNITSUNA(Material.DIAMOND_SWORD, "Kunitsuna", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 5)
            .enchant(Enchantment.SHARPNESS, 4)
            .enchant(Enchantment.FIRE_ASPECT, 1)),
    EXODIA(Material.DIAMOND_SWORD, "Exodia", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 5)
            .enchant(Enchantment.SHARPNESS, 4)
            .enchant(Enchantment.FIRE_ASPECT, 2)),
    ZANGETSU(Material.DIAMOND_SWORD, "Zangetsu", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 5)
            .enchant(Enchantment.SHARPNESS, 5)
            .enchant(Enchantment.FIRE_ASPECT, 2)),
    ASTRALITH(Material.DIAMOND_SWORD, "Astralith", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 6)
            .enchant(Enchantment.SHARPNESS, 5)
            .enchant(Enchantment.FIRE_ASPECT, 2)),
    THUAN(Material.DIAMOND_SWORD, "Thuan", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 6)
            .enchant(Enchantment.SHARPNESS, 5)
            .enchant(Enchantment.FIRE_ASPECT, 3)),
    ZENTETSUKEN(Material.DIAMOND_SWORD, "Zentetsuken", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 6)
            .enchant(Enchantment.SHARPNESS, 6)
            .enchant(Enchantment.FIRE_ASPECT, 3)),
    SHIZUMARU(Material.DIAMOND_SWORD, "Shizumaru", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 7)
            .enchant(Enchantment.SHARPNESS, 6)
            .enchant(Enchantment.FIRE_ASPECT, 3)),
    BUDOKARA(Material.DIAMOND_SWORD, "Budokara", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.SMITE, 7)
            .enchant(Enchantment.SHARPNESS, 6)
            .enchant(Enchantment.FIRE_ASPECT, 4)),
    AETHERIUM(Material.DIAMOND_SWORD, "Aetherium", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.SMITE, 7)
            .enchant(Enchantment.SHARPNESS, 7)
            .enchant(Enchantment.FIRE_ASPECT, 4)),
    PRISMATIC_BREAKER(Material.DIAMOND_SWORD, "Prismatic Breaker", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.SMITE, 8)
            .enchant(Enchantment.SHARPNESS, 7)
            .enchant(Enchantment.FIRE_ASPECT, 4)),
    ATARAXIA(Material.NETHERITE_SWORD, "Ataraxia", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 8)
            .enchant(Enchantment.SHARPNESS, 7)
            .enchant(Enchantment.FIRE_ASPECT, 5)),
    BRIGHTROAR(Material.NETHERITE_SWORD, "Brightroar", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 8)
            .enchant(Enchantment.SHARPNESS, 8)
            .enchant(Enchantment.FIRE_ASPECT, 5)),
    MURASAME(Material.NETHERITE_SWORD, "Murasame", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 3).enchant(Enchantment.SMITE, 9)
            .enchant(Enchantment.SHARPNESS, 8)
            .enchant(Enchantment.FIRE_ASPECT, 5)),
    ELUCIDATOR(Material.NETHERITE_SWORD, "Elucidator", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 9)
            .enchant(Enchantment.SHARPNESS, 9)
            .enchant(Enchantment.FIRE_ASPECT, 5)),
    ASHURA(Material.NETHERITE_SWORD, "Ashura", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 10)
            .enchant(Enchantment.SHARPNESS, 9)
            .enchant(Enchantment.FIRE_ASPECT, 6)),
    NIGHTS_EDGE(Material.NETHERITE_SWORD, "Night's Edge", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.SMITE, 10)
            .enchant(Enchantment.SHARPNESS, 10)
            .enchant(Enchantment.FIRE_ASPECT, 6)),
    DARK_REPULSOR(Material.NETHERITE_SWORD, "Dark Repulsor", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 11)
            .enchant(Enchantment.SHARPNESS, 10)
            .enchant(Enchantment.FIRE_ASPECT, 6).hideEnchants()),
    ASHBRINGER(Material.NETHERITE_SWORD, "Ashbringer", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 11)
            .enchant(Enchantment.SHARPNESS, 11)
            .enchant(Enchantment.FIRE_ASPECT, 6).hideEnchants()),
    LUXURIA(Material.NETHERITE_SWORD, "Luxuria", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 12)
            .enchant(Enchantment.SHARPNESS, 11)
            .enchant(Enchantment.FIRE_ASPECT, 7).hideEnchants()),
    ASTRAL_BLADE(Material.NETHERITE_SWORD, "Astral Blade", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 5).enchant(Enchantment.SMITE, 12)
            .enchant(Enchantment.SHARPNESS, 12)
            .enchant(Enchantment.FIRE_ASPECT, 7).hideEnchants()),
    TYRFING(Material.NETHERITE_SWORD, "Tyrfing", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.SMITE, 13)
            .enchant(Enchantment.SHARPNESS, 13)
            .enchant(Enchantment.FIRE_ASPECT, 7).ability(AbilityType.LIGHTNING_STRIKE)
            .hideEnchants().unbreakable()),
    KURIKARA(Material.NETHERITE_SWORD, "Kurikara", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 7).enchant(Enchantment.SMITE, 14)
            .enchant(Enchantment.SHARPNESS, 14)
            .enchant(Enchantment.FIRE_ASPECT, 7).ability(AbilityType.HEALING_TOUCH)
            .hideEnchants().unbreakable()),

    // Armor - Adventurer
    ADVENTURER_HELMET(Material.IRON_HELMET, "Adventurer Helmet", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2)),
    ADVENTURER_CHESTPLATE(Material.IRON_CHESTPLATE, "Adventurer Chestplate", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2)),
    ADVENTURER_LEGGINGS(Material.IRON_LEGGINGS, "Adventurer Leggings", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2)),
    ADVENTURER_BOOTS(Material.IRON_BOOTS, "Adventurer Boots", ItemRarity.COMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 2)),
    // Armor - Hunter
    HUNTER_HELMET(Material.IRON_HELMET, "Hunter Helmet", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.PROTECTION, 1)),
    HUNTER_CHESTPLATE(Material.IRON_CHESTPLATE, "Hunter Chestplate", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.PROTECTION, 1)),
    HUNTER_LEGGINGS(Material.IRON_LEGGINGS, "Hunter Leggings", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.PROTECTION, 1)),
    HUNTER_BOOTS(Material.IRON_BOOTS, "Hunter Boots", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 4).enchant(Enchantment.PROTECTION, 1)),
    // Armor - Olympian
    OLYMPIAN_HELMET(Material.IRON_HELMET, "Olympian Helmet", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.PROTECTION, 2)),
    OLYMPIAN_CHESTPLATE(Material.IRON_CHESTPLATE, "Olympian Chestplate", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.PROTECTION, 2)),
    OLYMPIAN_LEGGINGS(Material.IRON_LEGGINGS, "Olympian Leggings", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.PROTECTION, 2)),
    OLYMPIAN_BOOTS(Material.IRON_BOOTS, "Olympian Boots", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 6).enchant(Enchantment.PROTECTION, 2)),
    // Armor - Slayer
    SLAYER_HELMET(Material.IRON_HELMET, "Slayer Helmet", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 8).enchant(Enchantment.PROTECTION, 3)),
    SLAYER_CHESTPLATE(Material.IRON_CHESTPLATE, "Slayer Chestplate", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 8).enchant(Enchantment.PROTECTION, 3)),
    SLAYER_LEGGINGS(Material.IRON_LEGGINGS, "Slayer Leggings", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 8).enchant(Enchantment.PROTECTION, 3)),
    SLAYER_BOOTS(Material.IRON_BOOTS, "Slayer Boots", ItemRarity.UNCOMMON,
        new Attributes().enchant(Enchantment.UNBREAKING, 8).enchant(Enchantment.PROTECTION, 3)),
    // Armor - Titanium
    TITANIUM_HELMET(Material.DIAMOND_HELMET, "Titanium Helmet", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 10).enchant(Enchantment.PROTECTION, 3)),
    TITANIUM_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "Titanium Chestplate", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 10).enchant(Enchantment.PROTECTION, 3)),
    TITANIUM_LEGGINGS(Material.DIAMOND_LEGGINGS, "Titanium Leggings", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 10).enchant(Enchantment.PROTECTION, 3)),
    TITANIUM_BOOTS(Material.DIAMOND_BOOTS, "Titanium Boots", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 10).enchant(Enchantment.PROTECTION, 3)),
    // Armor - Prismatic
    PRISMATIC_HELMET(Material.DIAMOND_HELMET, "Prismatic Helmet", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 15).enchant(Enchantment.PROTECTION, 4)
            .hideEnchants()),
    PRISMATIC_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "Prismatic Chestplate", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 15).enchant(Enchantment.PROTECTION, 4)
            .hideEnchants()),
    PRISMATIC_LEGGINGS(Material.DIAMOND_LEGGINGS, "Prismatic Leggings", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 15).enchant(Enchantment.PROTECTION, 4)
            .hideEnchants()),
    PRISMATIC_BOOTS(Material.DIAMOND_BOOTS, "Prismatic Boots", ItemRarity.RARE,
        new Attributes().enchant(Enchantment.UNBREAKING, 15).enchant(Enchantment.PROTECTION, 4)
            .hideEnchants()),
    // Armor - Lunar
    LUNAR_HELMET(Material.DIAMOND_HELMET, "Lunar Helmet", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 35).enchant(Enchantment.PROTECTION, 5)
            .hideEnchants()),
    LUNAR_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "Lunar Chestplate", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 35).enchant(Enchantment.PROTECTION, 5)
            .hideEnchants()),
    LUNAR_LEGGINGS(Material.DIAMOND_LEGGINGS, "Lunar Leggings", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 35).enchant(Enchantment.PROTECTION, 5)
            .hideEnchants()),
    LUNAR_BOOTS(Material.DIAMOND_BOOTS, "Lunar Boots", ItemRarity.EPIC,
        new Attributes().enchant(Enchantment.UNBREAKING, 35).enchant(Enchantment.PROTECTION, 5)
            .hideEnchants()),
    // Armor - Molten
    MOLTEN_HELMET(Material.NETHERITE_HELMET, "Molten Helmet", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 75).enchant(Enchantment.PROTECTION, 6)
            .hideEnchants()),
    MOLTEN_CHESTPLATE(Material.NETHERITE_CHESTPLATE, "Molten Chestplate", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 75).enchant(Enchantment.PROTECTION, 6)
            .hideEnchants()),
    MOLTEN_LEGGINGS(Material.NETHERITE_LEGGINGS, "Molten Leggings", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 75).enchant(Enchantment.PROTECTION, 6)
            .hideEnchants()),
    MOLTEN_BOOTS(Material.NETHERITE_BOOTS, "Molten Boots", ItemRarity.LEGENDARY,
        new Attributes().enchant(Enchantment.UNBREAKING, 75).enchant(Enchantment.PROTECTION, 6)
            .hideEnchants()),
    // Armor - Dark Matter
    DARK_MATTER_HELMET(Material.NETHERITE_HELMET, "Dark Matter Helmet", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 200).enchant(Enchantment.PROTECTION, 7)
            .hideEnchants().unbreakable()),
    DARK_MATTER_CHESTPLATE(Material.NETHERITE_CHESTPLATE, "Dark Matter Chestplate", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 200).enchant(Enchantment.PROTECTION, 7)
            .hideEnchants().unbreakable()),
    DARK_MATTER_LEGGINGS(Material.NETHERITE_LEGGINGS, "Dark Matter Leggings", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 200).enchant(Enchantment.PROTECTION, 7)
            .hideEnchants().unbreakable()),
    DARK_MATTER_BOOTS(Material.NETHERITE_BOOTS, "Dark Matter Boots", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.UNBREAKING, 200).enchant(Enchantment.PROTECTION, 7)
            .hideEnchants().unbreakable()),

    SAYNDALES(Material.GOLDEN_BOOTS, "Sayndales", ItemRarity.MYTHICAL,
        new Attributes().enchant(Enchantment.PROTECTION, 6).enchant(Enchantment.FEATHER_FALLING, 4)
            .enchant(Enchantment.UNBREAKING, 10).unbreakable().hideAttributes().speed(0.3)
            .ability(AbilityType.DASH),
        "&9+0.3 Speed"),
    MANA_BOTTLE(Material.EXPERIENCE_BOTTLE, "Mana Bottle", ItemRarity.EPIC, "&dRestores your mana!",
        "&dUse to gain 40 mana points."),
    HEALING_POTION_L1(Material.POTION, "Healing Potion", ItemRarity.NONE, new Attributes().heal(0)),
    HEALING_POTION_L2(Material.POTION, "Healing Potion", ItemRarity.NONE, new Attributes().heal(1)),
    ARROW(Material.ARROW, "Arrow", ItemRarity.NONE),

    EXPLORER_SWORD(Material.STONE_SWORD, "Explorer Sword", ItemRarity.COMMON),
    EXPLORER_HELMET(Material.LEATHER_HELMET, "Explorer Helmet", ItemRarity.COMMON),
    EXPLORER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "Explorer Chestplate", ItemRarity.COMMON),
    EXPLORER_LEGGINGS(Material.LEATHER_LEGGINGS, "Explorer Leggings", ItemRarity.COMMON),
    EXPLORER_BOOTS(Material.LEATHER_BOOTS, "Explorer Boots", ItemRarity.COMMON),

    GUIDE_SWORD(Material.STONE_SWORD, "Guide Sword", ItemRarity.COMMON),
    GUIDE_HELMET(Material.LEATHER_HELMET, "Guide Helmet", ItemRarity.COMMON),
    GUIDE_CHESTPLATE(Material.LEATHER_CHESTPLATE, "Guide Chestplate", ItemRarity.COMMON),
    GUIDE_LEGGINGS(Material.LEATHER_LEGGINGS, "Guide Leggings", ItemRarity.COMMON),
    GUIDE_BOOTS(Material.LEATHER_BOOTS, "Guide Boots", ItemRarity.COMMON);

    private final Material material;
    private final String name;
    private final ItemRarity rarity;
    private final List<String> lore;
    private final Attributes attributes;

    AnimalQuestItem(Material material, String name, ItemRarity rarity, String... lore) {
        this(material, name, rarity, null, lore);
    }

    AnimalQuestItem(Material material, String name, ItemRarity rarity, Attributes attributes, String... lore) {
        this.material = material;
        this.name = name;
        this.rarity = rarity;
        this.attributes = attributes;
        this.lore = Arrays.asList(lore);
    }

    public ItemStack getItemStack(int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (rarity != ItemRarity.NONE && rarity != ItemRarity.COMMON && rarity != ItemRarity.UNCOMMON
                && rarity != ItemRarity.RARE) {
                meta.setDisplayName(Utils.applyFormat(rarity.getColor() + "&l" + name));
            } else {
                meta.setDisplayName(Utils.applyFormat(rarity.getColor() + name));
            }

            List<String> fullLore = new ArrayList<>();
            for (String line : lore) {
                fullLore.add(Utils.applyFormat(line));
            }

            if (rarity != ItemRarity.NONE) {
                fullLore.add(Utils.applyFormat(
                    rarity.getColor() + "&l" + rarity.getDisplayName().toUpperCase()));
            }

            meta.setLore(fullLore);

            if (attributes != null) {
                attributes.apply(item, meta);
            }

            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public String getName() {
        return Utils.applyFormat(name);
    }

    private static class Attributes {
        private final Map<Enchantment, Integer> enchants = new HashMap<>();
        private boolean unbreakable = false;
        private boolean hideEnchants = false;
        private boolean hideAttributes = false;
        private AttributeModifier speedModifier = null;
        private PotionEffect potionEffect = null;
        private AbilityType ability = null;

        public Attributes ability(AbilityType ability) {
            this.ability = ability;
            return this;
        }

        public Attributes heal(int level) {
            potionEffect = new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, level);
            return this;
        }

        public Attributes enchant(Enchantment enchantment, int level) {
            enchants.put(enchantment, level);
            return this;
        }

        public Attributes unbreakable() {
            unbreakable = true;
            return this;
        }

        public Attributes hideEnchants() {
            hideEnchants = true;
            return this;
        }

        public Attributes hideAttributes() {
            hideAttributes = true;
            return this;
        }

        public Attributes speed(double value) {
            speedModifier = new AttributeModifier(new NamespacedKey("animalquest", "speed_boost"), value,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET);
            return this;
        }

        public void apply(ItemStack item, ItemMeta meta) {
            List<String> enchantmentLore = new ArrayList<>();
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
                if (hideEnchants) {
                    String enchantmentName = formatEnchantmentName(
                        entry.getKey().getKey().getKey());
                    String enchantmentLevel = toRoman(entry.getValue());
                    enchantmentLore.add(Utils
                        .applyFormat("&7" + enchantmentName + " " + enchantmentLevel));
                }
            }

            if (meta.hasLore()) {
                enchantmentLore.addAll(meta.getLore());
            }

            if (!enchantmentLore.isEmpty())
                meta.setLore(enchantmentLore);

            if (unbreakable)
                meta.setUnbreakable(true);
            if (hideEnchants)
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (hideAttributes)
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (speedModifier != null)
                meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedModifier);
            if (potionEffect != null && meta instanceof PotionMeta potionMeta)
                potionMeta.addCustomEffect(potionEffect, true);
            if (ability != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add("");
                lore.add(Utils.applyFormat(ability.getDisplayName()));
                lore.add(Utils.applyFormat(ability.getDescription()));
                meta.setLore(lore);
            }
        }

        private String formatEnchantmentName(String name) {
            String[] parts = name.split("_");
            StringBuilder sb = new StringBuilder();
            for (String part : parts) {
                sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase())
                    .append(" ");
            }
            return sb.toString().trim();
        }

        private String toRoman(int level) {
            String[] roman = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII",
                "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"};
            return (level > 0 && level < roman.length) ? roman[level] : String.valueOf(level);
        }
    }
}
