package com.campmongoose.serversaturday.command.sscommand;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.AbstractCommand;
import com.campmongoose.serversaturday.command.CommandArgument;
import com.campmongoose.serversaturday.command.HelpCommand;
import com.campmongoose.serversaturday.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.command.sscommand.submit.SSGetRewards;
import com.campmongoose.serversaturday.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.command.sscommand.view.SSViewDescription;
import java.util.Arrays;
import java.util.Collections;
import org.bukkit.command.CommandSender;

public class SSCommand extends AbstractCommand
{
    public SSCommand()
    {
        super("ss", "Plugin based submission form for Potato's Server Saturday.", Collections.singletonList(new CommandArgument(Commands.SS_CMD)), 0, "", false, Arrays.asList(new SSDescription(), new SSEdit(), new SSFeature(), new SSGetRewards(), new SSGiveReward(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSetRewards(), new SSSubmit(), new SSView(), new SSViewDescription()));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("help"))
                return new HelpCommand(this).onCommand(sender, moveArguments(args));

            for (AbstractCommand command : getSubCommands())
                if (command.getName().equalsIgnoreCase(args[0]))
                    return command.onCommand(sender, moveArguments(args));
        }

        return new HelpCommand(this).onCommand(sender, moveArguments(args));
    }
}
