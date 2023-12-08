package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.sponge.command.argument.BuildValueParser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class SSSubmit extends ServerSaturdayCommand {

    @NotNull
    @Override
    public String name() {
        return "submit";
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public String description() {
        return "Submit your build to be featured.";
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss submit <build>";
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        if (context.subject() instanceof ServerPlayer player) {
            return context.one(BuildValueParser.VALUE).map(map -> {
                Build build = map.get(player.uniqueId());
                if (build == null) {
                    return CommandResult.error(Messages.BUILD_DOES_NOT_EXIST);
                }

                build.setSubmitted(!build.submitted());
                player.sendMessage(text(Messages.PREFIX + "Build " + (build.submitted() ? "has been submitted." : " is no longer submitted."), GREEN));
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
