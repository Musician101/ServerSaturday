package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.SSCommandException;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSReload extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        try {
            getSubmissions().save();
            getSubmissions().load();
            SpongeServerSaturday.instance().getConfig().reload();
            source.sendMessage(Text.builder(Messages.PLUGIN_RELOADED).color(TextColors.GOLD).build());
            return CommandResult.success();
        }
        catch (SSCommandException e) {
            source.sendMessage(Text.builder(e.getMessage()).color(TextColors.RED).build());
        }

        return CommandResult.empty();
    }
}
