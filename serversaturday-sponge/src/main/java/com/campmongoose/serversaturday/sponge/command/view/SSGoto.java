package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.command.SpongeCommandExecutor;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSGoto extends SpongeCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<String>getOne(Commands.PLAYER).map(playerName -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                SpongeSubmitter submitter = null;
                try {
                    submitter = getSubmissions().getSubmitter(UUIDUtils.getUUIDOf(playerName));
                }
                catch (IOException e) {
                    for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                        if (s.getName().equalsIgnoreCase(playerName)) {
                            submitter = s;
                        }
                    }
                }

                if (submitter == null) {
                    player.sendMessage(Text.builder(Messages.PLAYER_NOT_FOUND).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                Optional<String> name = arguments.getOne(Commands.BUILD);
                if (name.isPresent()) {
                    SpongeBuild build = submitter.getBuild(name.get());
                    if (build == null) {
                        player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                        return CommandResult.empty();
                    }

                    player.setLocation(build.getLocation());
                    player.sendMessage(Text.builder(Messages.teleportedToBuild(build)).color(TextColors.GOLD).build());
                    return CommandResult.success();
                }

                return CommandResult.empty();
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
