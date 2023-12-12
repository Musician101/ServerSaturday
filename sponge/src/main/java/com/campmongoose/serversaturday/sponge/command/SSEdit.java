package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.gui.EditBuildGUI;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static com.campmongoose.serversaturday.sponge.command.argument.SubmitterBuildValueParser.VALUE;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class SSEdit extends ServerSaturdayCommand {

    @NotNull
    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss edit " + VALUE.usage(cause));
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Edit a submitted build.", GRAY);
    }

    @Override
    public @NotNull List<Parameter> getParameters() {
        return List.of(VALUE);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.SUBMIT);
    }

    @Override
    public CommandResult execute(CommandContext context) {
        ServerPlayer player = (ServerPlayer) context.cause();
        Submitter submitter = getSubmitter(player);
        Build build = context.requireOne(VALUE);
        new EditBuildGUI(build, submitter, player);
        return CommandResult.success();
    }
}
