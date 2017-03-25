package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSLocation extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<String>getOne(Commands.BUILD).map(name -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                SpongeSubmitter submitter = getSubmitter(player);
                SpongeBuild build = submitter.getBuild(name);
                if (build == null) {
                    player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                build.setLocation(player.getLocation());
                new BuildGUI(build, submitter, player, null);
                player.sendMessage(Text.builder(Messages.locationChanged(build)).color(TextColors.GREEN).build());
                return CommandResult.success();
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
