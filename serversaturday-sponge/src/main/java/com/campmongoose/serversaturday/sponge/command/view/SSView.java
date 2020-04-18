package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmittersGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.AbstractMap.SimpleEntry;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class SSView extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return arguments.<SpongeSubmitter>getOne(SubmitterCommandElement.KEY).map(submitter -> {
                new SubmitterGUI(submitter, player);
                return CommandResult.success();
            }).orElseGet(() -> arguments.<SimpleEntry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterBuildCommandElement.KEY).map(entry -> {
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                if (player.getUniqueId().equals(submitter.getUUID())) {
                    new EditBuildGUI(build, submitter, player);
                }
                else {
                    new ViewBuildGUI(build, submitter, player);
                }

                return CommandResult.success();
            }).orElseGet(() -> {
                new SubmittersGUI(player);
                return CommandResult.success();
            }));
        }

        return playerOnly(source);
    }
}
