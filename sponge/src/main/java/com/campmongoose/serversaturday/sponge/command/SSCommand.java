package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

public class SSCommand extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        CommandCause cause = context.cause();
        cause.sendMessage(Identity.nil(), Component.join(Component.text(" "), Component.text("=====").color(NamedTextColor.GREEN), Component.text(Reference.NAME + " v" + Reference.VERSION).color(NamedTextColor.WHITE), Component.text("by").color(NamedTextColor.GREEN), Component.text("Musician101").color(NamedTextColor.WHITE), Component.text("=====").color(NamedTextColor.GREEN)));
        String shortPrefix = "ss";
        Stream.of(shortPrefix, shortPrefix + "e", shortPrefix + "v", shortPrefix + "gr", shortPrefix + "getr", shortPrefix + "r").forEach(s -> Sponge.server().commandManager().commandMapping(s).filter(cm -> cm.plugin().metadata().id().equals(Reference.ID) && cm.registrar().canExecute(cause, cm)).flatMap(cm -> cm.registrar().help(cause, cm)).ifPresent(component -> cause.sendMessage(Identity.nil(), component)));
        return CommandResult.success();
    }
}
