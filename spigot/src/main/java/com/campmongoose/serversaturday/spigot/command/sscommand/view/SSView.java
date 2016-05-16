package com.campmongoose.serversaturday.spigot.command.sscommand.view;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.AbstractCommandArgument.Syntax;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

public class SSView extends AbstractSpigotCommand
{
    public SSView()
    {
        super(Commands.VIEW_NAME, Commands.VIEW_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.VIEW_NAME), new SpigotCommandArgument(Commands.PLAYER, Syntax.OPTIONAL, Syntax.REPLACE), new SpigotCommandArgument(Commands.BUILD, Syntax.OPTIONAL, Syntax.REPLACE))), new SpigotCommandPermissions(Permissions.VIEW, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)//NOSONAR
    {
        if (!canSenderUseCommand(sender))
            return false;

        Player player = (Player) sender;
        SpigotSubmissions submissions = SpigotServerSaturday.getInstance().getSubmissions();
        if (args.length > 0)
        {
            SpigotSubmitter submitter = null;
            try
            {
                submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(args[0]));
            }
            catch (IOException e)//NOSONAR
            {
                for (SpigotSubmitter s : submissions.getSubmitters())
                    if (s.getName().equalsIgnoreCase(args[0]))
                        submitter = s;
            }

            if (submitter == null)
            {
                player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                return false;
            }

            if (args.length > 1)
            {
                SpigotBuild build = submitter.getBuild(combineStringArray(moveArguments(args)));
                if (build == null)
                {
                    player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                    return false;
                }

                build.openMenu(submitter, player.getUniqueId());
                return true;
            }

            submitter.openMenu(1, player.getUniqueId());
        }
        else
            SpigotServerSaturday.getInstance().getSubmissions().openMenu(1, player.getUniqueId());

        return true;
    }
}
