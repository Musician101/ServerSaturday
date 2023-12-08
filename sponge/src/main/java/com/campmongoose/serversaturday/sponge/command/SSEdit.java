package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.command.argument.BuildValueParser;
import com.campmongoose.serversaturday.sponge.gui.EditBuildGUI;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SSEdit extends ServerSaturdayCommand {

    @NotNull
    @Override
    public String name() {
        return "edit";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss edit <build>";
    }

    @NotNull
    @Override
    public String description() {
        return "Edit a submitted build.";
    }

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        if (context.subject() instanceof ServerPlayer player) {
            Submitter submitter = getSubmitter(player);
            return context.one(BuildValueParser.VALUE).map(map -> {
                Build build = map.get(submitter.getUUID());
                if (build == null) {
                    return CommandResult.error(Messages.BUILD_DOES_NOT_EXIST);
                }

                new EditBuildGUI(build, submitter, player);
                return CommandResult.success();
            }).orElse(CommandResult.error(Messages.BUILD_DOES_NOT_EXIST));
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().executor(this).permission(Permissions.SUBMIT).shortDescription(Component.text(description())).addParameter(BuildValueParser.VALUE).build();
    }
}
