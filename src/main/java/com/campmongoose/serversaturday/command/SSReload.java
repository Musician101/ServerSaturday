package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public class SSReload implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        try {
            getPlugin().messages().load();
            getSubmissions().load();
            getPlugin().reload();
            sendMessage(context, Component.translatable("ss.command.reload.success"));
        }
        catch (IOException e) {
            ComponentLike argument = Argument.tagResolver(Placeholder.unparsed("error", e.getMessage()));
            Component message = Component.translatable("ss.command.reload.fail", argument);
            sendMessage(context, message);
            getPlugin().getComponentLogger().error(message);
        }

        return 1;
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss reload");
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.reload.description");
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return sender.getSender().hasPermission("ss.admin");
    }
}
