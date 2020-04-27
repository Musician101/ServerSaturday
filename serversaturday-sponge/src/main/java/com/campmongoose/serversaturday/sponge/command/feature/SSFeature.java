package com.campmongoose.serversaturday.sponge.command.feature;

import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.AllSubmissionsGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.SubmitterGUI;
import com.campmongoose.serversaturday.sponge.gui.chest.ViewBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;

public class SSFeature extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            return arguments.<SpongeSubmitter>getOne(SubmitterBuildCommandElement.KEY).map(submitter -> {
                new SubmitterGUI(submitter, player);
                return CommandResult.success();
            }).orElseGet(() -> arguments.<Entry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterBuildCommandElement.KEY).map(entry -> {
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                build.setFeatured(!build.featured());
                if (player.getUniqueId().equals(submitter.getUUID())) {
                    new EditBuildGUI(build, submitter, player);
                    return CommandResult.success();
                }

                new ViewBuildGUI(build, submitter, player);
                return CommandResult.success();
            }).orElseGet(() -> {
                new AllSubmissionsGUI(player);
                return CommandResult.success();
            }));
        }

        return playerOnly(source);
    }
}
