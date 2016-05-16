package com.campmongoose.serversaturday.spigot.command.sscommand.submit;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.AbstractCommandArgument.Syntax;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SSSubmit extends AbstractSpigotCommand
{
    public SSSubmit()
    {
        super(Commands.SUBMIT_NAME, Commands.SUBMIT_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.SUBMIT_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1), new SpigotCommandPermissions(Permissions.SUBMIT, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        String name = combineStringArray(args);
        SpigotSubmitter submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(player.getUniqueId());
        if (submitter.getBuild(name) == null)
        {
            player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
            return false;
        }

        SpigotBuild build = submitter.getBuild(name);
        build.setSubmitted(!build.submitted());
        build.openMenu(submitter, player.getUniqueId());
        return true;
    }
}
