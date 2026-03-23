package ca.pandaaa.animalquest.guis;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.MountType;
import ca.pandaaa.animalquest.player.Mount;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountShopGUI extends AnimalQuestGUI {

    private static final String TITLE = Utils.applyFormat("&8Shop &8&l>> &8Mounts");
    private final Map<Integer, MountType> slotToMount = new HashMap<>();
    private final Player player;
    private final PlayerData data;

    public MountShopGUI(Player player) {
        super(18, TITLE);
        this.player = player;
        this.data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        buildItems();
    }

    private void buildItems() {
        inventory.setItem(0, makeLabel(Material.HAY_BLOCK, "&e&lGround Mounts",
                "&7Companions for land travel."));
        inventory.setItem(9, makeLabel(Material.PRISMARINE, "&9&lWater Mounts",
                "&7Companions for ocean travel."));

        int groundSlot = 1;
        for (MountType mount : MountType.values()) {
            if (!mount.isInWater()) {
                inventory.setItem(groundSlot, buildMountItem(mount));
                slotToMount.put(groundSlot, mount);
                groundSlot++;
                if (groundSlot >= 8)
                    break;
            }
        }

        int waterSlot = 10;
        for (MountType mount : MountType.values()) {
            if (mount.isInWater()) {
                inventory.setItem(waterSlot, buildMountItem(mount));
                slotToMount.put(waterSlot, mount);
                waterSlot++;
                if (waterSlot >= 17)
                    break;
            }
        }

        ItemStack filler = makeLabel(Material.GRAY_STAINED_GLASS_PANE, " ", "");
        for (int i = 0; i < 18; i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, filler);
        }
    }

    private int currentTier(MountType mount) {
        if (data == null)
            return 1;
        Mount mounts = data.getMounts();
        return mount.isInWater() ? mounts.getWaterMount().getTier() : mounts.getGroundMount().getTier();
    }

    private boolean isOwned(MountType mount) {
        return currentTier(mount) >= mount.getTier();
    }

    private boolean isLocked(MountType mount) {
        return mount.getTier() > 1 && currentTier(mount) < mount.getTier() - 1;
    }

    private ItemStack buildMountItem(MountType mount) {
        boolean owned = isOwned(mount);
        boolean locked = !owned && isLocked(mount);

        Material icon;
        if (locked) {
            icon = Material.BARRIER;
        } else if (mount.isInWater()) {
            icon = Material.NAUTILUS_SHELL;
        } else {
            icon = Material.SADDLE;
        }

        double price = (mount.getTier() - 1) * 5000.0;
        String name = Utils.getSentenceCase(mount.name());

        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        if (owned) {
            meta.setDisplayName(Utils.applyFormat("&a&l✔ " + name + " &a(Owned)"));
        } else if (locked) {
            meta.setDisplayName(Utils.applyFormat("&c&l🔒 " + name + " &c(Locked)"));
        } else {
            meta.setDisplayName(Utils.applyFormat("&6&l" + name));
        }

        List<String> lore = new ArrayList<>();
        lore.add(Utils.applyFormat("&eTier: " + getTierStars(mount.getTier())));
        lore.add(Utils.applyFormat("&bSpeed: &f" + String.format("%.0f%%", mount.getSpeed() * 400)));
        if (!mount.isInWater()) {
            lore.add(Utils.applyFormat("&aJump: &f" + String.format("%.0f%%", mount.getJumpStrength() * 100)));
        }
        lore.add(Utils.applyFormat("&3Cooldown: &f" + mount.getCooldown() + "s"));
        lore.add("");

        if (owned) {
            lore.add(Utils.applyFormat("&a&lYou already own this mount."));
        } else if (locked) {
            int neededTier = mount.getTier() - 1;
            String neededName = getNameOfTier(mount.isInWater(), neededTier);
            lore.add(Utils.applyFormat("&c&lRequires&c: &f" + neededName));
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Purchase the previous tier first ))"));
        } else {
            lore.add(Utils.applyFormat("&b&lPrice&b: &3$" + (int) price));
            lore.add("");
            lore.add(Utils.applyFormat("&7&o(( Click to purchase ))"));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return getMenuItem(item, false);
    }

    private String getNameOfTier(boolean water, int tier) {
        for (MountType m : MountType.values()) {
            if (m.isInWater() == water && m.getTier() == tier) {
                return Utils.getSentenceCase(m.name());
            }
        }
        return "Tier " + tier;
    }

    private String getTierStars(int tier) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < tier ? "&6★" : "&8★");
        }
        return sb.toString();
    }

    private ItemStack makeLabel(Material mat, String name, String desc) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;
        meta.setDisplayName(Utils.applyFormat(name));
        if (!desc.isEmpty()) {
            List<String> lore = new ArrayList<>();
            lore.add(Utils.applyFormat("&7" + desc));
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void open() {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isEventRelevant(event.getView().getTopInventory()))
            return;
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(event.isShiftClick());
            return;
        }
        event.setCancelled(true);

        MountType mount = slotToMount.get(event.getSlot());
        if (mount == null)
            return;

        Player player = (Player) event.getWhoClicked();
        purchaseMount(player, mount);
    }

    private void purchaseMount(Player player, MountType mount) {
        if (data == null)
            return;

        if (isOwned(mount)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou already own the &6"
                    + Utils.getSentenceCase(mount.name()) + "&c mount!"));
            return;
        }

        if (isLocked(mount)) {
            String neededName = getNameOfTier(mount.isInWater(), mount.getTier() - 1);
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must own the &6" + neededName
                    + "&c mount first!"));
            return;
        }

        double price = (mount.getTier() - 1) * 5000.0;
        if (data.getBalance() < price) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou need &4$" + (int) price
                    + "&c to purchase this mount! (Balance: &4$" + (int) data.getBalance() + "&c)"));
            return;
        }

        data.setBalance(data.getBalance() - price);

        data.getStatistics().logPurchase("MOUNT_" + mount.name());

        Mount mounts = data.getMounts();
        if (mount.isInWater()) {
            mounts.setWaterMount(mount);
        } else {
            mounts.setGroundMount(mount);
        }

        player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bSuccessfully purchased the &6"
                + Utils.getSentenceCase(mount.name()) + "&b mount for &3$" + (int) price + "&b."));
        player.closeInventory();
    }
}
