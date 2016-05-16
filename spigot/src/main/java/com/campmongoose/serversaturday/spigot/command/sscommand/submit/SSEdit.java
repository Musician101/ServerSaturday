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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SSEdit extends AbstractSpigotCommand
{
    public SSEdit()
    {
        super(Commands.EDIT_NAME, Commands.EDIT_DESC, new SpigotCommandUsage(Arrays.asList(new SpigotCommandArgument(Commands.SS_CMD), new SpigotCommandArgument(Commands.EDIT_NAME), new SpigotCommandArgument(Commands.BUILD, Syntax.REPLACE, Syntax.OPTIONAL))), new SpigotCommandPermissions(Permissions.SUBMIT, true));
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        Player player = (Player) sender;
        SpigotSubmitter submitter = SpigotServerSaturday.getInstance().getSubmissions().getSubmitter(player.getUniqueId());
        if (args.length > 0)
        {
            String name = combineStringArray(args);
            if (submitter.getBuild(name) == null)
            {
                player.sendMessage(ChatColor.RED + Messages.BUILD_NOT_FOUND);
                return false;
            }

            submitter.getBuild(name).openMenu(submitter, player.getUniqueId());
            return true;
        }

        submitter.openMenu(1, player.getUniqueId());
        return true;
    }
}
