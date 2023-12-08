package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class SSNew extends ServerSaturdayCommand {

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public String description() {
        return "Create a new build to be submitted.";
    }

    @NotNull
    @Override
    public String name() {
        return "new";
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss new <name>";
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) throws CommandException {
        if (context.subject() instanceof ServerPlayer player) {
            return context.one(value).map(s -> {
                getSubmitter(player).newBuild(s, SpongeSubmissionsUtil.asSSLocation(player));
                player.sendMessage(text(Messages.PREFIX + "New build created successfully.", GREEN));
                return CommandResult.success();
            }).orElse(CommandResult.error(Component.text(Messages.PREFIX + "Not enough arguments.", RED)));
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    private final Value<String> value = Parameter.remainingJoinedStrings().key(Commands.BUILD).build();

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().executor(this).shortDescription(Component.text(description())).permission(Permissions.SUBMIT).addParameter(value).build();
    }
}
