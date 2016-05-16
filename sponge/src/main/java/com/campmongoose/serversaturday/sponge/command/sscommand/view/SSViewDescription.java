package com.campmongoose.serversaturday.sponge.command.sscommand.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.AbstractCommandArgument.Syntax;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
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
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SSViewDescription extends AbstractSpongeCommand
{
    public SSViewDescription()
    {
        super(Commands.VIEW_DESCRIPTION_NAME, Commands.VIEW_DESCRIPTION_DESC, new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(Commands.SS_CMD), new SpongeCommandArgument(Commands.VIEW_DESCRIPTION_NAME), new SpongeCommandArgument(Commands.PLAYER, Syntax.REQUIRED, Syntax.REPLACE), new SpongeCommandArgument(Commands.BUILD, Syntax.REQUIRED, Syntax.REPLACE)), 2), new SpongeCommandPermissions(Permissions.VIEW, true));
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments)//NOSONAR
    {
        String[] args = splitArgs(arguments);
        if (!testPermission(source))
            return CommandResult.empty();

        if (!minArgsMet(source, args.length))
            return CommandResult.empty();

        Player player = (Player) source;
        SpongeSubmitter submitter = null;
        try
        {
            submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(args[0]));
        }
        catch (IOException e)//NOSONAR
        {
            for (SpongeSubmitter s : SpongeServerSaturday.instance().getSubmissions().getSubmitters())
                if (s.getName().equalsIgnoreCase(args[0]))
                    submitter = s;
        }

        if (submitter == null)
        {
            player.sendMessage(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
            return CommandResult.empty();
        }

        SpongeBuild build = submitter.getBuild(combineStringArray(splitArgs(moveArguments(args))));
        if (build == null)
        {
            player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
            return CommandResult.empty();
        }

        BookView.Builder bvb = BookView.builder();
        bvb.author(Text.of(submitter.getName()));
        bvb.addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList()));
        bvb.title(Text.of(build.getName()));
        player.sendBookView(bvb.build());
        return CommandResult.success();
    }
}
