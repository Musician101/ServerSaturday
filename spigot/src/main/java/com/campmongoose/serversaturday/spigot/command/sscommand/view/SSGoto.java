package com.campmongoose.serversaturday.spigot.command.sscommand.view;

import com.campmongoose.serversaturday.spigot.ServerSaturday;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.AbstractCommandArgument.Syntax;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class SSGoto extends AbstractSpigotCommand
{
    public SSGoto()
    {
        super(Commands.GOTO_NAME, Commands.GOTO_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.GOTO_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 2), new SpigotCommandPermissions(Permissions.VIEW_GOTO, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)//NOSONAR
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        SpigotSubmitter submitter = null;
        try
        {
            submitter = ServerSaturday.getInstance().getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(args[0]));
        }
        catch (IOException e)//NOSONAR
        {
            for (SpigotSubmitter s : ServerSaturday.getInstance().getSubmissions().getSubmitters())
                if (s.getName().equalsIgnoreCase(args[0]))
                    submitter = s;
        }

        if (submitter == null)
        {
            player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
            return false;
        }

        SpigotBuild build = submitter.getBuild(combineStringArray(moveArguments(args)));
        if (build == null)
        {
            player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
            return false;
        }

        player.teleport(build.getLocation());
        player.sendMessage(ChatColor.GOLD + Messages.teleportedToBuild(build));
        return true;
    }
}
