package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.HelpCommand;
import com.campmongoose.serversaturday.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.command.sscommand.view.SSViewDescription;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;

public class SSCommand extends AbstractCommand
{
    public SSCommand(ServerSaturday plugin)
    {
        super(plugin, "ss", "Plugin based submission form for Potato's Server Saturday.", Collections.singletonList(new CommandArgument(Commands.SS_CMD)), 0, "", false, Arrays.asList(new SSDescription(plugin), new SSEdit(plugin), new SSFeature(plugin), new SSGoto(plugin), new SSLocation(plugin), new SSNew(plugin), new SSReload(plugin), new SSRename(plugin), new SSResourcePack(plugin), new SSSubmit(plugin), new SSView(plugin), new SSViewDescription(plugin)));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("help"))
                return new HelpCommand(plugin, this).onCommand(sender, moveArguments(args));

            for (AbstractCommand command : getSubCommands())
                if (command.getName().equalsIgnoreCase(args[0]))
                    return command.onCommand(sender, moveArguments(args));
        }

        return new HelpCommand(plugin, this).onCommand(sender, moveArguments(args));
    }
}
