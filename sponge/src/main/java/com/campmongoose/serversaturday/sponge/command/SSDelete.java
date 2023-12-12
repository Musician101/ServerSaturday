package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
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
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SSDelete extends ServerSaturdayCommand {

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        ServerPlayer player = (ServerPlayer) context.cause();
        Submitter submitter = getSubmitter(player);
        Build build = context.requireOne(VALUE);
        submitter.getBuilds().remove(build);
        player.sendMessage(text(Messages.PREFIX + "Build deleted.", GREEN));
        return CommandResult.success();
    }

    @Override
    public @NotNull List<Parameter> getParameters() {
        return List.of(VALUE);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.SUBMIT);
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Delete a submission.", GRAY);
    }

    @NotNull
    @Override
    public String getName() {
        return "delete";
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss delete " + VALUE.usage(cause));
    }
}
