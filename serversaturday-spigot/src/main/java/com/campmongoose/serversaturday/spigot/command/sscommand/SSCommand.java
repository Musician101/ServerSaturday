package com.campmongoose.serversaturday.spigot.command.sscommand;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.spigot.command.AbstractSpigotCommand;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandArgument;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandPermissions;
import com.campmongoose.serversaturday.spigot.command.SpigotCommandUsage;
import com.campmongoose.serversaturday.spigot.command.SpigotHelpCommand;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewDescription;
import java.util.Arrays;
import java.util.Collections;
import org.bukkit.command.CommandSender;

public class SSCommand extends AbstractSpigotCommand {

    public SSCommand() {
        super(Commands.SS_CMD.replace("/", ""), Reference.DESCRIPTION, new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD))), new SpigotCommandPermissions("", false), Arrays.asList(new SSDescription(), new SSEdit(), new SSFeature(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription()));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(Commands.HELP_NAME)) {
                return new SpigotHelpCommand(this).onCommand(sender, moveArguments(args));
            }

            for (AbstractSpigotCommand command : getSubCommands()) {
                if (command.getName().equalsIgnoreCase(args[0])) {
                    return command.onCommand(sender, moveArguments(args));
                }
            }
        }

        return new SpigotHelpCommand(this).onCommand(sender, moveArguments(args));
    }
}
