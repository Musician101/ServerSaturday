package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
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
        SpongeSubmissions submissions = getSubmissions();
        submissions.save();
        submissions.load();
        getPlugin().getConfig().reload();
        source.sendMessage(Text.of(TextColors.GOLD, Messages.PLUGIN_RELOADED));
        return CommandResult.success();
    }
}
