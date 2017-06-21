package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmissionsGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.AbstractMap.SimpleEntry;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class SSView extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return arguments.<SpongeSubmitter>getOne(SubmitterCommandElement.KEY).map(submitter -> {
                new SubmitterGUI(player, submitter, 1, null);
                return CommandResult.success();
            }).orElseGet(() -> arguments.<SimpleEntry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterBuildCommandElement.KEY).map(entry -> {
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                if (player.getUniqueId().equals(submitter.getUUID())) {
                    new EditBuildGUI(build, submitter, player, null);
                }
                else {
                    new ViewBuildGUI(entry.getValue(), entry.getKey(), player, null);
                }

                return CommandResult.success();
            }).orElseGet(() -> {
                new SubmissionsGUI(player, 1, null);
                return CommandResult.success();
            }));
        }

        return playerOnly(source);
    }
}
