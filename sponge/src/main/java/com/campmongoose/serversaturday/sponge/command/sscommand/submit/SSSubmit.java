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
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SSSubmit extends AbstractSpongeCommand
{
    public SSSubmit()
    {
        super(Commands.SUBMIT_NAME, Commands.SUBMIT_DESC, new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(Commands.SS_CMD), new SpongeCommandArgument(Commands.SUBMIT_NAME), new SpongeCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 1), new SpongeCommandPermissions(Permissions.SUBMIT, true));
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
        if (submitter.getBuild(name) == null)
        {
            player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
            return CommandResult.empty();
        }

        SpongeBuild build = submitter.getBuild(name);
        build.setSubmitted(!build.submitted());
        build.openMenu(submitter, player.getUniqueId());
        return CommandResult.success();
    }
}
