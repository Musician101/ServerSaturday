package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSNew extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) throws CommandException {
        if (source instanceof Player) {
            Player player = (Player) source;
            SpongeSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SSCommandException e) {
                player.sendMessage(Text.join(Text.of(Messages.PREFIX), Text.builder(e.getMessage()).color(TextColors.RED).build()));
                return CommandResult.empty();
            }

            int maxBuilds = getPluginInstance().getConfig().getMaxBuilds();
            if (submitter.getBuilds().size() > maxBuilds && maxBuilds > 0 && !player.hasPermission(Permissions.EXCEED_MAX_BUILDS)) {
                player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                return CommandResult.empty();
            }

            return arguments.<String>getOne(Commands.NAME).map(name -> {
                if (submitter.getBuild(name) != null) {
                    player.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                SpongeBuild build = submitter.newBuild(name, player.getLocation());
                new EditBuildGUI(build, submitter, player, null);
                return CommandResult.success();
            }).orElse(CommandResult.success());
        }

        return playerOnly(source);
    }
}
