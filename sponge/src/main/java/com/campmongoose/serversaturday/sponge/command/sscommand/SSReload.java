package com.campmongoose.serversaturday.sponge.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandArgument;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandPermissions;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandUsage;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class SSReload extends AbstractSpongeCommand
{
    public SSReload()
    {
        super(Commands.RELOAD_NAME, Commands.RELOAD_DESC, new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(Commands.SS_CMD), new SpongeCommandArgument(Commands.RELOAD_NAME))), new SpongeCommandPermissions(Permissions.RELOAD, false));
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments)
    {
        if (!testPermission(source))
            return CommandResult.empty();

        SpongeServerSaturday.instance().getSubmissions().save();
        SpongeServerSaturday.instance().getSubmissions().load();
        source.sendMessage(Text.builder(Messages.PLUGIN_RELOADED).color(TextColors.GOLD).build());
        return CommandResult.success();
    }
}
