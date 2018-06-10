package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class SSGiveReward extends SpigotCommand {

    public SSGiveReward() {
        super(Commands.GIVE_REWARD_NAME, Commands.GIVE_REWARD_DESCRIPTION);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.GIVE_REWARD_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.FEATURE, false);
        executor = (sender, args) -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.get(0));
            UUID uuid = null;
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUniqueId();
            }

            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                return false;
            }

            getPluginInstance().getRewardGiver().addReward(uuid);
            sender.sendMessage(ChatColor.GOLD + Messages.ERROR);
            return true;
        };
    }
}
