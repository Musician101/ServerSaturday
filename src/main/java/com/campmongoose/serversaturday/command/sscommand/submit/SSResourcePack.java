package com.campmongoose.serversaturday.command.sscommand.submit;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.CommandArgument.Syntax;
import com.campmongoose.serversaturday.menu.anvil.ResourcePackChangeMenu;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.campmongoose.serversaturday.util.UUIDCacheException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSResourcePack extends AbstractCommand {

    public SSResourcePack() {
        super("resourcepack", "Change the recommended resource pack.", Arrays.asList(new CommandArgument(Commands.SS_CMD), new CommandArgument("resourcepack"), new CommandArgument("build", Syntax.REQUIRED, Syntax.REPLACE)), 1, "ss.submit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, String... args) {
        if (!canSenderUseCommand(sender)) {
            return false;
        }

        if (!minArgsMet(sender, args.length)) {
            return false;
        }

        Player player = (Player) sender;
        String name = StringUtils.join(args, " ");
        try {
            Submitter submitter = getSubmitter(player);
            if (submitter.getBuild(name) == null) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "A build with that name does not exist.");
                return false;
            }

            Build build = submitter.getBuild(name);
            new ResourcePackChangeMenu(build, player.getUniqueId()).open(player);
            return true;
        }
        catch (UUIDCacheException e) {
            player.sendMessage(e.getMessage());
            return false;
        }
    }
}
