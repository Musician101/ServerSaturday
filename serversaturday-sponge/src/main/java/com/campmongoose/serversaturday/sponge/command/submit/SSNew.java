package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.gui.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.gui.textinput.TextInput;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSNew extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        if (source instanceof Player) {
            Player player = (Player) source;
            SpongeSubmitter submitter = getSubmitter(player);
            int maxBuilds = getPluginInstance().getConfig().getMaxBuilds();
            if (submitter.getBuilds().size() > maxBuilds && maxBuilds > 0 && !player.hasPermission(Permissions.EXCEED_MAX_BUILDS)) {
                player.sendMessage(Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build());
                return CommandResult.empty();
            }

            return arguments.<String>getOne(Commands.NAME)
                    .map(name -> {
                if (submitter.getBuild(name) != null) {
                    player.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
                    return CommandResult.empty();
                }

                SpongeBuild build = submitter.newBuild(name, player.getLocation());
                new BuildGUI(build, submitter, player, null);
                return CommandResult.success();
            }).orElseGet(() -> {
                new TextInput(player, null) {

                    @Override
                    protected void build() {
                        player.sendMessage(Text.builder(Messages.PREFIX + "Please type the name of the build.").color(TextColors.RED).build());
                        biFunction = (string, player) -> {
                            if (string.equalsIgnoreCase("/cancel")) {
                                if (prevMenu != null) {
                                    prevMenu.open();
                                }

                                return null;
                            }

                            if (submitter.getBuild(string) != null) {
                                player.sendMessage(Text.builder(Messages.BUILD_ALREADY_EXISTS).color(TextColors.RED).build());
                                return null;
                            }

                            SpongeBuild build = submitter.newBuild(string, player.getLocation());
                            new BuildGUI(build, submitter, player, null);
                            return string;
                        };
                    }
                };

                return CommandResult.success();
            });
        }

        return playerOnly(source);
    }
}
