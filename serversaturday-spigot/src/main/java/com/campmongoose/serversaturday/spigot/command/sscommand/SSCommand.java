package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSGetRewards;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewResourcePack;
import java.util.Collections;
import java.util.stream.Stream;
import org.bukkit.ChatColor;

public class SSCommand extends AbstractSpigotCommand {

    public SSCommand() {
        super(Commands.SS_CMD.replace("/", ""), Commands.HELP_DESC);
        usage = new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD)));
        permissions = new SpigotCommandPermissions("", false);
        executor = (sender, args) -> {
            sender.sendMessage(ChatColor.GREEN + "===== " + ChatColor.RESET + Reference.NAME + " v" + SpigotServerSaturday.instance().getDescription().getVersion() + ChatColor.GREEN + " by " + ChatColor.RESET + "Musician101" + ChatColor.GREEN + " =====");
            sender.sendMessage(getHelp());
            Stream.of(new SSDescription(), new SSEdit(), new SSFeature(), new SSGetRewards(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription(), new SSViewResourcePack())
                    .filter(command -> sender.hasPermission(command.getPermissions().getPermissionNode())).forEach(command -> sender.sendMessage(command.getHelp()));

            return true;
        };
    }
}
