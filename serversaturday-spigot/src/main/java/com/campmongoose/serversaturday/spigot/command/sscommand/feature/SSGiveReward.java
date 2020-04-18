package com.campmongoose.serversaturday.spigot.command.sscommand.feature;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.Syntax;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSGiveReward extends SpigotCommand {

    public SSGiveReward() {
        super(Commands.GIVE_REWARD_NAME, Commands.GIVE_REWARD_DESC);
        usage = new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD + Commands.GIVE_REWARD_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE)), 1);
        permissions = new SpigotCommandPermissions(Permissions.FEATURE, false);
        executor = (sender, args) -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.get(0));
            getPluginInstance().getRewardGiver().addReward(offlinePlayer.getUniqueId());
            sender.sendMessage(ChatColor.GOLD + Messages.rewardsGiven(args.get(0)));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(ChatColor.GOLD + Messages.REWARDS_WAITING);
            }

            return true;
        };
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (permissions.testPermission(sender)) {
            if (args.length == 1) {
                return getSubmissions().getSubmitters().stream().map(SpigotSubmitter::getName).filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}
