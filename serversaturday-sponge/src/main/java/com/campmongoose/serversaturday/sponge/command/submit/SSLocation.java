package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.build.EditBuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
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
        return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                build.setLocation(player.getLocation());
                try {
                    new EditBuildGUI(build, getSubmitter(player), player, null);
                    player.sendMessage(Text.builder(Messages.locationChanged(build)).color(TextColors.GREEN).build());
                    return CommandResult.success();
                }
                catch (SSCommandException e) {
                    player.sendMessage(Text.builder(e.getMessage()).color(TextColors.RED).build());
                    return CommandResult.empty();
                }
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
