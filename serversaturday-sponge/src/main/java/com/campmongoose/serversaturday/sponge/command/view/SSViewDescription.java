package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSViewDescription extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<String>getOne(Commands.PLAYER).map(playerName -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                SpongeSubmitter submitter = getSubmitter(playerName);

                if (submitter == null) {
                    player.sendMessage(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                return arguments.<String>getOne(Commands.BUILD).map(name -> {
                    SpongeBuild build = submitter.getBuild(name);
                    if (build == null) {
                        player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                        return CommandResult.empty();
                    }

                    BookView.Builder bvb = BookView.builder();
                    bvb.author(Text.of(submitter.getName()));
                    bvb.addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList()));
                    bvb.title(Text.of(build.getName()));
                    player.sendBookView(bvb.build());
                    return CommandResult.success();
                }).orElse(CommandResult.empty());
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
