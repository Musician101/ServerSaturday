package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmissionsGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSView extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return arguments.<String>getOne(Commands.PLAYER).map(playerName -> {
                SpongeSubmitter submitter = getSubmitter(playerName);
                if (submitter == null) {
                    player.sendMessage(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                return arguments.<String>getOne(Commands.BUILD).map(buildName -> {
                    SpongeBuild build = submitter.getBuild(buildName);
                    if (build == null) {
                        player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                        return CommandResult.empty();
                    }

                    new BuildGUI(build, submitter, player, null);
                    return CommandResult.success();
                }).orElseGet(() -> {
                    new SubmitterGUI(player, submitter, 1, null);
                    return CommandResult.success();
                });
            }).orElseGet(() -> {
                new SubmissionsGUI(player, 1, null);
                return CommandResult.success();
            });
        }

        return playerOnly(source);
    }
}
