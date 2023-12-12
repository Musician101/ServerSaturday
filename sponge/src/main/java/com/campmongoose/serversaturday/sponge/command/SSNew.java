package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSNew extends ServerSaturdayCommand {

    private final Value<String> value = Parameter.remainingJoinedStrings().key(Commands.BUILD).build();

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        ServerPlayer player = (ServerPlayer) context.cause();
        String name = context.requireOne(value);
        getSubmitter(player).newBuild(name, SpongeSubmissionsUtil.asSSLocation(player));
        player.sendMessage(text(Messages.PREFIX + "New build created successfully.", GREEN));
        return CommandResult.success();
    }

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("Create a new build to be submitted.", GRAY);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.SUBMIT);
    }

    @NotNull
    @Override
    public String getName() {
        return "new";
    }

    @Override
    public @NotNull List<Parameter> getParameters() {
        return List.of(value);
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss new " + value.usage(cause));
    }
}
