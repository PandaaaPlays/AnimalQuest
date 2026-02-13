package ca.pandaaa.animalquest.spells;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CraftsmansAnvil extends Spell {

    public CraftsmansAnvil() {
        super("craftsmans_anvil", "Craftsman's Anvil", 70, 15, "Repairs your armor.",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTNlMzJhNmM0MTQ3ZGY0MGQwMjM4NTQ2ZDM2ZDVjMWVlYWZjMmVhOWNlYWMwMzUzZmNiODNiMGVlYTJkMmNmMSJ9fX0=");
    }

    @Override
    public void cast(Player player) {
        if (player.getInventory().getHelmet() != null) {
            player.getInventory().getHelmet().setDurability((short) 0);
        }

        if (player.getInventory().getChestplate() != null) {
            player.getInventory().getChestplate().setDurability((short) 0);
        }

        if (player.getInventory().getLeggings() != null) {
            player.getInventory().getLeggings().setDurability((short) 0);
        }

        if (player.getInventory().getBoots() != null) {
            player.getInventory().getBoots().setDurability((short) 0);
        }

        player.getLocation().getWorld().spawnParticle(Particle.ENCHANT, player.getLocation(), 40, 1.0D, 1.0D,
            1.0D);
    }
}
