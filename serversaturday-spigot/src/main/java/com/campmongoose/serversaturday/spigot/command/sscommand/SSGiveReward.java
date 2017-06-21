package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.uuid.MojangAPIException;
import com.campmongoose.serversaturday.spigot.uuid.PlayerNotFoundException;
import com.campmongoose.serversaturday.spigot.uuid.UUIDCacheException;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.ChatColor;

public class SSGiveReward extends AbstractSpigotCommand {

    public SSGiveReward() {
        super(Commands.GIVE_REWARD_NAME, Commands.GIVE_REWARD_DESCRIPTION);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.GIVE_REWARD_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.FEATURE, false);
        executor = (sender, args) -> {
            UUID uuid;
            try {
                uuid = getUUIDCache().getUUIDOf(args.get(0));
            }
            catch (IOException | MojangAPIException | PlayerNotFoundException | UUIDCacheException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }

            getPluginInstance().getRewardGiver().addReward(uuid);
            sender.sendMessage(ChatColor.GOLD + Messages.ERROR);
            return true;
        };
    }
}
