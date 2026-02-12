package ca.pandaaa.animalquest.spells;

import org.bukkit.entity.Player;

public class Fireball extends Spell {

    public Fireball() {
        super("fireball", "Fireball", 5, 2, "Shoots a fireball where you are looking.");
    }

    @Override
    public void cast(Player player) {
        org.bukkit.entity.Fireball fireball = player.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }
}
