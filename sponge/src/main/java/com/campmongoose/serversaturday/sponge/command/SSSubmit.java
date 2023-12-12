package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import java.util.List;
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
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSSubmit extends ServerSaturdayCommand {

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        ServerPlayer player = (ServerPlayer) context.cause();
        Build build = context.requireOne(VALUE);
        if (build == null) {
            return CommandResult.error(Messages.BUILD_DOES_NOT_EXIST);
        }

        build.setSubmitted(!build.submitted());
        player.sendMessage(text(Messages.PREFIX + "Build " + (build.submitted() ? "has been submitted." : " is no longer submitted."), GREEN));
        return CommandResult.success();
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Submit your build to be featured.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "submit";
    }

    @Override
    public @NotNull List<Parameter> getParameters() {
        return List.of(VALUE);
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss submit " + VALUE.usage(cause));
    }
}
