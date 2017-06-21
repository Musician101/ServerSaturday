package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSEdit extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            SpongeSubmitter submitter;
            try {
                submitter = getSubmitter(player);
            }
            catch (SSCommandException e) {
                player.sendMessage(Text.builder(e.getMessage()).color(TextColors.RED).build());
                return CommandResult.empty();
            }

            return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
                new EditBuildGUI(build, submitter, player, null);
                return CommandResult.success();
            }).orElseGet(() -> {
                new SubmitterGUI(player, submitter, 1, null);
                return CommandResult.success();
            });
        }

        return playerOnly(source);
    }
}
