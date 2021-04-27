package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Submitter;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.io.IOException;
import javax.annotation.Nonnull;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

public class SSReload extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        DataStorage<?, Submitter<Component>> submissions = getSubmissions();
        submissions.save();
        submissions.load();
        try {
            getPlugin().getConfig().reload();
            context.sendMessage(Identity.nil(), Component.text(Messages.PLUGIN_RELOADED).color(NamedTextColor.GOLD));
            return CommandResult.success();
        }
        catch (IOException e) {
            context.sendMessage(Identity.nil(), Component.text("").color(NamedTextColor.RED));
            return CommandResult.empty();
        }
    }
}
