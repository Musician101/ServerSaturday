package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.build.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class SSFeature extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return arguments.<SpongeSubmitter>getOne(SubmitterBuildCommandElement.KEY).map(submitter -> {
                new SubmitterGUI(player, submitter, 1, null);
                return CommandResult.success();
            }).orElseGet(() -> arguments.<Entry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterBuildCommandElement.KEY).map(entry -> {
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                build.setFeatured(!build.featured());
                new ViewBuildGUI(build, submitter, player, null);
                return CommandResult.success();
            }).orElseGet(() -> {
                new AllSubmissionsGUI(player, 1, null);
                return CommandResult.success();
            }));
        }

        return playerOnly(source);
    }
}
