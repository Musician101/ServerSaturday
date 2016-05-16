package com.campmongoose.serversaturday.sponge.command.sscommand;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.SpigotHelpCommand;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandArgument;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandPermissions;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandUsage;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.sponge.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.sponge.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.sponge.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.sponge.command.sscommand.view.SSViewDescription;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

public class SSCommand extends AbstractSpongeCommand
{
    public SSCommand()
    {
        super(Commands.SS_CMD.replace("/", ""), Reference.DESCRIPTION, new SpongeCommandUsage(Collections.singletonList(new SpongeCommandArgument(Commands.SS_CMD))), new SpongeCommandPermissions("", false), Arrays.asList(new SSDescription(), new SSEdit(), new SSFeature(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription()));
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments) throws CommandException
    {
        String[] args = splitArgs(arguments);
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase(Commands.HELP_NAME))
                return new SpigotHelpCommand(this).process(source, moveArguments(args));

            for (AbstractSpongeCommand command : getSubCommands())
                if (command.getName().equalsIgnoreCase(args[0]))
                    return command.process(source, moveArguments(args));
        }

        return new SpigotHelpCommand(this).process(source, moveArguments(args));
    }
}
