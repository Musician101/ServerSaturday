package com.campmongoose.serversaturday.sponge.command.sscommand;

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
import com.campmongoose.serversaturday.sponge.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;

public class SSFeature extends AbstractSpongeCommand
{
    public SSFeature()
    {
        super(Commands.FEATURE_NAME, Commands.FEATURE_DESC, new SpongeCommandUsage(Arrays.asList(new SpongeCommandArgument(Commands.SS_CMD), new SpongeCommandArgument(Commands.FEATURE_NAME), new SpongeCommandArgument(Commands.PLAYER, Syntax.OPTIONAL, Syntax.REPLACE), new SpongeCommandArgument(Commands.BUILD, Syntax.OPTIONAL, Syntax.REPLACE))), new SpongeCommandPermissions(Permissions.FEATURE, true));
    }

    @Nonnull
    @Override
    public CommandResult process(@Nonnull CommandSource source, @Nonnull String arguments)//NOSONAR
    {
        String[] args = splitArgs(arguments);
        if (!testPermission(source))
            return CommandResult.empty();

        Player player = (Player) source;
        if (args.length > 0)
        {
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

            if (args.length > 1)
            {
                SpongeBuild build = submitter.getBuild(combineStringArray(splitArgs(moveArguments(args))));
                if (build == null)
                {
                    player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                build.setFeatured(!build.featured());
                build.openMenu(submitter, player.getUniqueId());
                return CommandResult.success();
            }

            submitter.openMenu(1, player.getUniqueId());
            return CommandResult.success();
        }

        new AllSubmissionsMenu(1, player.getUniqueId()).open(player.getUniqueId());
        return CommandResult.success();
    }
}
