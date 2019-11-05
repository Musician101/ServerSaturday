package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.SpongeConfig;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSReload extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        getSubmissions().ifPresent(submissions -> {
            submissions.save();
            submissions.load();
        });
        SpongeServerSaturday.instance().map(SpongeServerSaturday.class::cast).map(SpongeServerSaturday::getConfig).ifPresent(SpongeConfig::reload);
        source.sendMessage(Text.of(TextColors.GOLD, Messages.PLUGIN_RELOADED));
        return CommandResult.success();
    }
}
