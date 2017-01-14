package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandExecutor;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmitterMenu;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSEdit extends SpongeCommandExecutor
{
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments)
    {
        if (source instanceof Player) {
            Player player = (Player) source;
            SpongeSubmitter submitter = getSubmitter(player);
            Optional<CommandResult> arg = arguments.<String>getOne(Commands.BUILD)
                    .map(name -> {
                        SpongeBuild build = submitter.getBuild(name);
                        if (build == null)
                        {
                            player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                            return CommandResult.empty();
                        }

                        new BuildMenu(build, submitter, player, null);
                        return CommandResult.success();
                    });

            if (arg.isPresent())
                return arg.get();

            new SubmitterMenu(player, submitter, 1, null);
            return CommandResult.success();
        }

        return playerOnly(source);
    }
}
