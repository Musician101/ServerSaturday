package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import javax.annotation.Nonnull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSCommand extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) throws CommandException {
        source.sendMessage(Text.join(Text.builder("===== ").color(TextColors.GREEN).build(), Text.of(Reference.NAME + " v" + Reference.VERSION), Text.builder(" by ").color(TextColors.GREEN).build(), Text.of("Musician101"), Text.builder(" =====").color(TextColors.GREEN).build()));
        Sponge.getCommandManager().getOwnedBy(SpongeServerSaturday.instance()).forEach(commandMapping -> {
            CommandCallable command = commandMapping.getCallable();
            command.getShortDescription(source).ifPresent(description ->
                    source.sendMessage(Text.join(Text.builder("/" + commandMapping.getPrimaryAlias() + " ").color(TextColors.GRAY).build(), command.getUsage(source), Text.of(" "), Text.builder().append(description).color(TextColors.AQUA).build())));
        });

        return CommandResult.success();
    }
}
