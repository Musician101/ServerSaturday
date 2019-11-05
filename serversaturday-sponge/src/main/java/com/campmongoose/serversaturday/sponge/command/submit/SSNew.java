package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIs;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSNew extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return getSubmitter(player).flatMap(submitter -> getPluginInstance().map(SpongeServerSaturday.class::cast).map(SpongeServerSaturday::getConfig).map(config -> {
                int maxBuilds = config.getMaxBuilds();
                if (submitter.getBuilds().size() > maxBuilds && maxBuilds > 0 && !player.hasPermission(Permissions.EXCEED_MAX_BUILDS)) {
                    player.sendMessage(Text.of(TextColors.RED, Messages.NO_PERMISSION));
                    return CommandResult.empty();
                }

                return arguments.<String>getOne(Commands.NAME).map(name -> {
                    if (submitter.getBuild(name) != null) {
                        player.sendMessage(Text.of(TextColors.RED, Messages.BUILD_ALREADY_EXISTS));
                        return CommandResult.empty();
                    }

                    SpongeBuild build = submitter.newBuild(name, player.getLocation());
                    SpongeChestGUIs.INSTANCE.editBuild(build, submitter, player);
                    return CommandResult.success();
                }).orElse(CommandResult.success());
            })).orElse(CommandResult.empty());
        }

        return playerOnly(source);
    }
}
