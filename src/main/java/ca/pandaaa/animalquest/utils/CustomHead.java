package ca.pandaaa.animalquest.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class CustomHead {

    public static ItemStack createHead(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (texture != null && !texture.isEmpty()) {
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            try {
                PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CustomHead");
                PlayerTextures playerTextures = playerProfile.getTextures();

                String urlString;
                if (texture.startsWith("eyJ")) {
                    String json = new String(Base64.getDecoder().decode(texture), StandardCharsets.UTF_8);
                    int urlStartIndex = json.indexOf("\"url\":\"") + 7;
                    int urlEndIndex = json.indexOf("\"", urlStartIndex);
                    urlString = json.substring(urlStartIndex, urlEndIndex);
                } else {
                    urlString = "https://textures.minecraft.net/texture/" + texture;
                }

                playerTextures.setSkin(new URL(urlString));
                playerProfile.setTextures(playerTextures);

                headMeta.setOwnerProfile(playerProfile);
                head.setItemMeta(headMeta);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return head;
    }
}
