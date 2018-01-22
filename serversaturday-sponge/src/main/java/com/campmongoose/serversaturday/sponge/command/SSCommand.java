package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSCommand extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) {
        source.sendMessage(Text.of(TextColors.GREEN, "===== ", TextColors.RESET, Reference.NAME + " v" + Reference.VERSION, TextColors.GREEN, " by ", TextColors.RESET, "Musician101", TextColors.GREEN, " ====="));
        Sponge.getCommandManager().getOwnedBy(SpongeServerSaturday.instance()).forEach(commandMapping -> {
            CommandCallable command = commandMapping.getCallable();
            command.getShortDescription(source).ifPresent(description ->
                    source.sendMessage(Text.of(TextColors.GRAY, "/" + commandMapping.getPrimaryAlias() + " ", command.getUsage(source), " ", TextColors.AQUA, description)));
        });

        return CommandResult.success();
    }
}
