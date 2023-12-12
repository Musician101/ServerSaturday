package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.command.argument.SubmitterValueParser;
import com.campmongoose.serversaturday.sponge.command.argument.ViewerBuildValueParser;
import com.campmongoose.serversaturday.sponge.gui.BuildGUI;
import com.campmongoose.serversaturday.sponge.gui.TextGUI;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class SSView extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        ServerPlayer player = (ServerPlayer) context.cause();
        return context.one(SubmitterValueParser.VALUE).map(submitter -> context.one(ViewerBuildValueParser.VALUE).map(build -> {
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

    @NotNull
    @Override
    public Component getDescription(@NotNull CommandCause cause) {
        return text("View a player's submission(s).", GRAY);
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of(Permissions.VIEW);
    }

    @Override
    public @NotNull List<Parameter> getParameters() {
        return List.of(Parameter.seqBuilder(SubmitterValueParser.VALUE).then(ViewerBuildValueParser.VALUE).optional().build());
    }

    @NotNull
    @Override
    public Component getUsage(@NotNull CommandCause cause) {
        return text("/ss view [" + SubmitterValueParser.VALUE.usage(cause) + "] " + ViewerBuildValueParser.VALUE.usage(cause));
    }

    @NotNull
    @Override
    public String getName() {
        return "view";
    }

    @Override
    public boolean canUse(@NotNull CommandCause cause) {
        return cause instanceof ServerPlayer;
    }
}
