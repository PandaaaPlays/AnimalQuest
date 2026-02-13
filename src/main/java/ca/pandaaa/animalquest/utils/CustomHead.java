package ca.pandaaa.animalquest.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CustomHead {

    public static ItemStack createHead(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (url != null && !url.isEmpty()) {
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            try {
                PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CustomHead");

                PlayerTextures playerTextures = playerProfile.getTextures();
                playerTextures.setSkin(new URL("https://textures.minecraft.net/texture/" + url));
                playerProfile.setTextures(playerTextures);

                headMeta.setOwnerProfile(playerProfile);
                head.setItemMeta(headMeta);
            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            }
        }

        return head;
    }
}
