package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public class SSMain implements PaperLiteralCommand.AdventureFormat, SSCommand {

    private static void cmdInfo(CommandContext<CommandSourceStack> context, PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> cmd) {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();
        TagResolver commandResolver = TagResolver.resolver(Placeholder.component("usage", cmd.usage(source)), Placeholder.component("description", cmd.description(source)));
        String message = "<usage> <dark_gray>- <gray><description>";
        sender.sendMessage(MiniMessage.miniMessage().deserialize(message, commandResolver));
    }

    //TODO Might move this to just the help command
    @Deprecated
    static void help(CommandContext<CommandSourceStack> context, PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike> root) {
        CommandSourceStack source = context.getSource();
        CommandSender sender = source.getSender();
        sender.sendMessage(header());
        root.children().stream().filter(cmd -> cmd.canUse(source)).forEach(cmd -> cmdInfo(context, cmd));
    }

    private static Component header() {
        PluginMeta pdf = getPlugin().getPluginMeta();
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        String authorsString = switch (last) {
            case 0 -> authors.getFirst();
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        };
        TagResolver headerResolver = TagResolver.resolver(Placeholder.parsed("authors", authorsString), Placeholder.parsed("display-name", pdf.getDisplayName()));
        String string = "<dark_green>> ===== <green><hover:show_text:'<color:#BDB76B>Developed by <authors>'><display-name></hover><dark_green> ===== <<newline><gold>Click a command for more info.<newLine><click:open_url:https://github.com/Musician101/ServerSaturday/wiki>Click here to visit our wiki.";
        return MiniMessage.miniMessage().deserialize(string, headerResolver);
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        help(context, this);
        return 1;
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new SSClaim(), new SSDelete(), new SSEdit(), new SSHelp(this), new SSMyBuilds(), new SSNew(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());
    }

    @Override
    public String name() {
        return "serversaturday";
    }
}
