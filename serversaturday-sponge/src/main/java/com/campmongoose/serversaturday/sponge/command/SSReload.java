package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSReload extends SpongeCommandExecutor
{
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments)
    {
        getSubmissions().save();
        getSubmissions().load();
        source.sendMessage(Text.builder(Messages.PLUGIN_RELOADED).color(TextColors.GOLD).build());
        return CommandResult.success();
    }
}
