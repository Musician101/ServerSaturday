package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.sponge.command.argument.BuildValueParser;
import com.campmongoose.serversaturday.sponge.command.argument.SubmitterValueParser;
import com.campmongoose.serversaturday.sponge.gui.BuildGUI;
import com.campmongoose.serversaturday.sponge.gui.TextGUI;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SSView extends ServerSaturdayCommand {

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().executor(this).permission(Permissions.SUBMIT).shortDescription(Component.text(description())).addParameter(SubmitterValueParser.VALUE).build();
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        if (context.subject() instanceof ServerPlayer player) {
            return context.one(SubmitterValueParser.VALUE).map(submitter -> context.one(BuildValueParser.VALUE).map(map -> {
                Build build = map.get(submitter.getUUID());
                if (build == null) {
                    return CommandResult.error(Messages.BUILD_DOES_NOT_EXIST);
                }

                BuildGUI.open(build, submitter, player);
                return CommandResult.success();
            }).orElseGet(() -> {
                TextGUI.displaySubmitter(player, submitter, 1);
                return CommandResult.success();
            })).orElseGet(() -> {
                TextGUI.displaySubmitters(player, 1);
                return CommandResult.success();
            });
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    @NotNull
    @Override
    public String description() {
        return "View a player's submission(s).";
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss view [player] [build]";
    }

    @NotNull
    @Override
    public String name() {
        return "view";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return context.subject() instanceof ServerPlayer && context.hasPermission(Permissions.VIEW);
    }
}
