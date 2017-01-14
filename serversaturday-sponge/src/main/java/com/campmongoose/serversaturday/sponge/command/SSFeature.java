package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.sponge.menu.chest.AllSubmissionsMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildMenu;
import com.campmongoose.serversaturday.sponge.menu.chest.SubmitterMenu;
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

public class SSFeature extends SpongeCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            Optional<String> playerName = arguments.getOne(Commands.PLAYER);
            if (playerName.isPresent()) {
                SpongeSubmitter submitter = null;
                try {
                    submitter = getSubmitter(UUIDUtils.getUUIDOf(playerName.get()));
                }
                catch (IOException e) {
                    for (SpongeSubmitter s : getSubmissions().getSubmitters()) {
                        if (s.getName().equalsIgnoreCase(playerName.get())) {
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

                    build.setFeatured(!build.featured());
                    new BuildMenu(build, submitter, player, null);
                    return CommandResult.success();
                }

                new SubmitterMenu(player, submitter, 1, null);
                return CommandResult.success();
            }

            new AllSubmissionsMenu(player, 1, null);
            return CommandResult.success();
        }

        return playerOnly(source);
    }
}
