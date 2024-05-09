package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.Bukkitier;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.help.HelpMainCommand;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class SSCommand extends HelpMainCommand {

    private SSCommand() {
        super(getPlugin());
    }

    public static void registerCommand() {
        Bukkitier.registerCommand(getPlugin(), new SSCommand());
    }

    @NotNull
    static Component cmdInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        String string = "<click:run_command:/ss help " + command.name() + ">/ss " + command.name() + " <dark_gray>- <gray>" + command.description(sender);
        return miniMessage().deserialize(string);
    }

    @SuppressWarnings("UnstableApiUsage")
    @NotNull
    @Override
    protected Component header() {
        PluginMeta pdf = plugin.getPluginMeta();
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        String authorsString = switch (last) {
            case 0 -> authors.getFirst();
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        };
        String string = "<dark_green>> ===== <green><hover:show_text:'<color:#BDB76B>Developed by " + authorsString + "'>" + pdf.getDisplayName() + "</hover><dark_green> ===== <<newline><gold>Click a command for more info.<newLine><click:open_url:https://github.com/Musician101/ServerSaturday/wiki>Click here to visit our wiki.";
        return miniMessage().deserialize(string);
    }

    @Override
    protected @NotNull Component commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        return cmdInfo(command, sender);
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSClaim(), new SSDelete(), new SSEdit(), new SSHelp(this), new SSMyBuilds(), new SSNew(), new SSReload(), new SSReward(), new SSSubmit(), new SSView(), new SSViewAll());
    }

    @NotNull
    @Override
    public String name() {
        return "serversaturday";
    }
}
