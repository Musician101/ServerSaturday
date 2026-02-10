package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public class SSHelp implements PaperLiteralCommand.AdventureFormat, SSCommand {

    private final SSMain root;

    public SSHelp(SSMain root) {
        this.root = root;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.help.description");
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss help");
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();
        sender.sendMessage(header());
        root.children().stream().filter(cmd -> cmd.canUse(source)).map(cmd -> cmdInfo(context, cmd)).forEach(sender::sendMessage);
        return 1;
    }

    private Component cmdInfo(CommandContext<CommandSourceStack> context, PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> cmd) {
        CommandSourceStack source = context.getSource();
        ComponentLike commandResolver = Argument.tagResolver(Placeholder.component("usage", cmd.usage(source)), Placeholder.component("description", cmd.description(source)));
        return Component.translatable("ss.command.help.command-info", commandResolver);
    }

    private Component header() {
        PluginMeta pdf = getPlugin().getPluginMeta();
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        String authorsString = switch (last) {
            case 0 -> authors.getFirst();
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        };

        ComponentLike headerResolver = Argument.tagResolver(Placeholder.unparsed("authors", authorsString), Placeholder.unparsed("display-name", pdf.getDisplayName()));
        return Component.translatable("ss.command.help.header", headerResolver);
    }
}
