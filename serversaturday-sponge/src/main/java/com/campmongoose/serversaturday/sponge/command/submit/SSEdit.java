package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class SSEdit extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            SpongeSubmitter submitter = getSubmitter(player);
            return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
                new EditBuildGUI(build, submitter, player);
                return CommandResult.success();
            }).orElseGet(() -> {
                new SubmitterGUI(submitter, player);
                return CommandResult.success();
            });
        }

        return playerOnly(source);
    }
}
