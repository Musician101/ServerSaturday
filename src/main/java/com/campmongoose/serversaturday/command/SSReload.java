package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

@NullMarked
public class SSReload implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        try {
            getSubmissions().load();
            getPlugin().reload();
            sendMessage(context, text(PREFIX + "Plugin reloaded. Check console for errors.", GOLD));
        }
        catch (IOException e) {
            //TODO need proper error message
            sendMessage(context, Component.text("Reload failed."));
        }

        return 1;
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss reload");
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.text("Reload the plugin.");
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
