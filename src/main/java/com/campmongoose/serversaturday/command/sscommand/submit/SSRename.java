package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.menu.anvil.NameChangeMenu;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SSRename extends AbstractCommand
{
    public SSRename(ServerSaturday plugin)
    {
        super(plugin, "rename", "Rename a submission.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("rename"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args)
    {
        if (!canSenderUseCommand(sender))
            return false;

        if (!minArgsMet(sender, args.length))
            return false;

        Player player = (Player) sender;
        String name = args[0];
        if (!plugin.getSubmissions().getSubmitter(player.getUniqueId()).containsBuild(name))
        {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
            return false;
        }

        Submitter submitter = plugin.getSubmissions().getSubmitter(player.getUniqueId());
        Build build = submitter.getBuild(name);
        new NameChangeMenu(plugin, build, player.getUniqueId()).open(player);
        return true;
    }
}
