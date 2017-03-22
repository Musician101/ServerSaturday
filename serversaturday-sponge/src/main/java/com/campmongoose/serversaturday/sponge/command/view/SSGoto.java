package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSGoto extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {

        if (source instanceof Player) {
            return arguments.<String>getOne(Commands.PLAYER).map(playerName -> {
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

                    player.setLocation(build.getLocation());
                    player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                    return CommandResult.success();
                }).orElse(CommandResult.empty());
            }).orElse(CommandResult.empty());
        }

        return playerOnly(source);
    }
}
