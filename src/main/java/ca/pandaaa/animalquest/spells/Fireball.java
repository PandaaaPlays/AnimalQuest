package ca.pandaaa.animalquest.spells;

import ca.pandaaa.animalquest.AnimalQuest;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Fireball extends Spell {

    public Fireball() {
        super("fireball", "Fireball", 5, 2, "Shoots a fireball where you are looking.",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM2ODdlMjVjNjMyYmNlOGFhNjFlMGQ2NGMyNGU2OTRjM2VlYTYyOWVhOTQ0ZjRjZjMwZGNmYjRmYmNlMDcxIn19fQ==");
    }

    @Override
    public void cast(Player player) {
        org.bukkit.entity.Fireball fireball = player.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setVelocity(player.getEyeLocation().getDirection().multiply(2));
        fireball.setMetadata("spell", new FixedMetadataValue(AnimalQuest.getPlugin(), "fireball"));
    }
}
