package com.campmongoose.serversaturday.paper.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.Bukkitier;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.help.HelpMainCommand;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;
import static net.md_5.bungee.api.ChatColor.DARK_GREEN;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.of;


@SuppressWarnings("deprecation")
public class SSCommand extends HelpMainCommand {

    private SSCommand() {
        super(getPlugin());
    }

    @SuppressWarnings("UnstableApiUsage")
    @NotNull
    @Override
    protected TextComponent header() {
        PluginMeta pdf = plugin.getPluginMeta();
        TextComponent begin = new TextComponent("> ===== ");
        begin.setColor(DARK_GREEN);
        TextComponent middle = new TextComponent(pdf.getDisplayName());
        middle.setColor(GREEN);
        TextComponent end = new TextComponent(" ===== <");
        end.setColor(DARK_GREEN);
        begin.addExtra(middle);
        begin.addExtra(end);
        TextComponent developed = new TextComponent("Developed by ");
        developed.setColor(GOLD);
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        TextComponent authorsComponent = new TextComponent(switch (last){
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        });
        authorsComponent.setColor(of("#BDB76B"));
        begin.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(new BaseComponent[]{developed, authorsComponent})));
        return begin;
    }

    @NotNull
    @Override
    protected TextComponent commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        TextComponent cmd = new TextComponent(command.usage(sender));
        TextComponent dash = new TextComponent(" - ");
        dash.setColor(ChatColor.DARK_GRAY);
        TextComponent description = new TextComponent(command.description(sender));
        description.setColor(ChatColor.GRAY);
        cmd.addExtra(dash);
        cmd.addExtra(description);
        return cmd;
    }

    public static void registerCommand() {
        Bukkitier.registerCommand(getPlugin(), new SSCommand());
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSClaim(), new SSDelete(), new SSEdit(), new SSMyBuilds(), new SSNew(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());
    }

    @NotNull
    @Override
    public String name() {
        return "serversaturday";
    }
}
