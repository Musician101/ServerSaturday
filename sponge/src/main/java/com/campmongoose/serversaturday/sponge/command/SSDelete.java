package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.command.argument.BuildValueParser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

class SSDelete extends ServerSaturdayCommand {

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        if (context.subject() instanceof ServerPlayer player) {
            Submitter submitter = getSubmitter(player);
            return context.one(BuildValueParser.VALUE).map(m -> m.get(player.uniqueId())).map(b -> {
                submitter.getBuilds().remove(b);
                player.sendMessage(text(Messages.PREFIX + "Build deleted.", GREEN));
                return CommandResult.success();
            }).orElse(CommandResult.error(Messages.BUILD_DOES_NOT_EXIST));
        }

        return CommandResult.error(Messages.PLAYER_ONLY_COMMAND);
    }

    @NotNull
    @Override
    public Command.Parameterized toCommand() {
        return Command.builder().permission(Permissions.SUBMIT).executor(this).shortDescription(Component.text(description())).addParameter(BuildValueParser.VALUE).build();
    }

    @Override
    public boolean canUse(@NotNull CommandContext context) {
        return canUseSubmit(context);
    }

    @NotNull
    @Override
    public String description() {
        return "Delete a submission.";
    }

    @NotNull
    @Override
    public String name() {
        return "delete";
    }

    @NotNull
    @Override
    public String usage() {
        return "/ss delete <player>";
    }
}
