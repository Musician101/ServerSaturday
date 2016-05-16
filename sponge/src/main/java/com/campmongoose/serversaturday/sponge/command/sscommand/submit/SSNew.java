package com.campmongoose.serversaturday.sponge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.AbstractCommandArgument.Syntax;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandArgument;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandPermissions;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandUsage;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SSNew extends AbstractSpongeCommand
{
    public SSNew()
    {
        super(Commands.NEW_NAME, Commands.NEW_DESC, new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(Commands.SS_CMD), new SpongeCommandArgument(Commands.NEW_NAME), new SpongeCommandArgument(Commands.NAME, Syntax.REPLACE, Syntax.REQUIRED)), 1), new SpongeCommandPermissions(Permissions.SUBMIT, true));
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments)
    {
        String[] args = splitArgs(arguments);
        if (!testPermission(source))
            return CommandResult.empty();

        if (!minArgsMet(source, args.length))
            return CommandResult.empty();

        Player player = (Player) source;
        String name = combineStringArray(args);
        SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(player.getUniqueId());
        if (submitter.getBuild(name) != null)
        {
            player.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
            return CommandResult.empty();
        }

        submitter.newBuild(name, player.getLocation()).openMenu(submitter, player.getUniqueId());
        return CommandResult.success();
    }
}
