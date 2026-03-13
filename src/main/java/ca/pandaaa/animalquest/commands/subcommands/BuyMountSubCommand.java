package ca.pandaaa.animalquest.commands.subcommands;

import ca.pandaaa.animalquest.AnimalQuest;
import ca.pandaaa.animalquest.enums.MountType;
import ca.pandaaa.animalquest.player.PlayerData;
import ca.pandaaa.animalquest.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BuyMountSubCommand implements SubCommand {

    @Override
    public String getName() {
        return "buymount";
    }

    @Override
    public String getDescription() {
        return "Confirm purchasing a mount.";
    }

    @Override
    public String getSyntax() {
        return "/animalquest buymount <mountName>";
    }

    @Override
    public String getPermission() {
        return "animalquest.player";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player))
            return;
        if (args.length < 2)
            return;

        String mountName = args[1].toUpperCase();
        if (!AnimalQuest.getPlugin().getNpcManager().consumePendingMount(player.getUniqueId(), mountName)) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must click the Mounts NPC to perform this action!"));
            return;
        }

        MountType mountType;
        try {
            mountType = MountType.valueOf(mountName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(Utils.applyFormat("&c&l[!] &cInvalid mount type!"));
            return;
        }

        PlayerData data = AnimalQuest.getPlugin().getPlayerDataManager().get(player.getUniqueId());
        if (data != null) {
            double price = (mountType.getTier() - 1) * 5000.0;

            boolean owned;
            int currentTier;
            if (mountType.isInWater()) {
                currentTier = data.getMounts().getWaterMount().getTier();
                owned = currentTier >= mountType.getTier();
            } else {
                currentTier = data.getMounts().getGroundMount().getTier();
                owned = currentTier >= mountType.getTier();
            }

            if (owned) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou already own the &6"
                        + Utils.getSentenceCase(mountType.name()) + "&c mount or a better one!"));
                return;
            }

            if (mountType.getTier() > 1 && currentTier < mountType.getTier() - 1) {
                String neededName = "";
                for (MountType m : MountType.values()) {
                    if (m.isInWater() == mountType.isInWater() && m.getTier() == mountType.getTier() - 1) {
                        neededName = Utils.getSentenceCase(m.name());
                        break;
                    }
                }
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou must own the &6" + neededName
                        + "&c mount first!"));
                return;
            }

            if (data.getBalance() < price) {
                player.sendMessage(Utils.applyFormat("&c&l[!] &cYou need &3$" + (int) price
                        + "&c to buy this mount! (Balance: &3$" + (int) data.getBalance() + "&c)"));
                return;
            }

            data.setBalance(data.getBalance() - price);

            if (mountType.isInWater()) {
                data.getMounts().setWaterMount(mountType);
            } else {
                data.getMounts().setGroundMount(mountType);
            }

            player.sendMessage(Utils.applyFormat(Utils.getAnimalQuestName() + " &7&l>> &bSuccessfully purchased the &6"
                    + Utils.getSentenceCase(mountType.name()) + "&b mount for &3$" + (int) price + "&b."));
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
