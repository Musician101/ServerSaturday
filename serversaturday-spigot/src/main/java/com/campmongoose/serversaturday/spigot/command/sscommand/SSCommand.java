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
import java.util.List;

public class SSCommand extends AbstractSpigotCommand {

    /**
     * @deprecated Temporarily hold sub commands here until command split is complete
     */
    @Deprecated
    private List<AbstractSpigotCommand> subCommands;

    public SSCommand() {
        super(Commands.SS_CMD.replace("/", ""), Reference.DESCRIPTION);
    }

    @Override
    protected void build() {
        usage =  new SpigotCommandUsage(Collections.singletonList(new SpigotCommandArgument(Commands.SS_CMD)));
        permissions = new SpigotCommandPermissions("", false);
        subCommands = Arrays.asList(new SSDescription(), new SSEdit(), new SSFeature(), new SSGoto(), new SSLocation(), new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription());
        executor = (sender, args) -> {
            if (!args.isEmpty()) {
                if (args.get(0).equalsIgnoreCase(Commands.HELP_NAME)) {
                    return new SpigotHelpCommand(this).onCommand(sender, moveArguments(args));
                }

                for (AbstractSpigotCommand command : subCommands) {
                    if (command.getName().equalsIgnoreCase(args.get(0))) {
                        return command.onCommand(sender, moveArguments(args));
                    }
                }
            }

            return new SpigotHelpCommand(this).onCommand(sender, moveArguments(args));
        };
    }

    /**
     * @deprecated Placeholder
     * @return
     */
    @Deprecated
    public List<AbstractSpigotCommand> getSubCommands() {
        return subCommands;
    }
}
