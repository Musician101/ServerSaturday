package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeChestGUIs;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSRemove extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<SpongeBuild>getOne(BuildCommandElement.KEY).map(build -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                return getSubmitter(player).map(submitter -> {
                    if (!submitter.removeBuild(build.getName())) {
                        player.sendMessage(Text.builder(Messages.BUILD_NOT_FOUND).color(TextColors.RED).build());
                        return CommandResult.empty();
                    }

                    SpongeChestGUIs.INSTANCE.submitter(1, player, submitter, null);
                    return CommandResult.success();
                }).orElse(CommandResult.empty());
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
