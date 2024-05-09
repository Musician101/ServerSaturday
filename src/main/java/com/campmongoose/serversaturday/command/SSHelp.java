package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import io.musician101.bukkitier.command.help.HelpSubCommand;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static com.campmongoose.serversaturday.command.SSCommand.cmdInfo;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class SSHelp extends HelpSubCommand {

    public SSHelp(@NotNull LiteralCommand root) {
        super(root, getPlugin());
    }

    @SuppressWarnings("UnstableApiUsage")
    @NotNull
    @Override
    protected Component header() {
        PluginMeta pdf = plugin.getPluginMeta();
        List<String> authors = pdf.getAuthors();
        int last = authors.size() - 1;
        String authorsString = switch (last) {
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        };
        String string = "<dark_green>> ===== <green><hover:show_text:'<color:#BDB76B>Developed by " + authorsString + "'>" + pdf.getDisplayName() + "</hover><dark_green> ===== <<newline><gold>Click a command to paste it below.<newLine><click:open_url:https://github.com/Musician101/ServerSaturday/wiki>Click here to visit our wiki.";
        return miniMessage().deserialize(string);
    }

    @Override
    protected @NotNull Component commandInfo(@NotNull Command<? extends ArgumentBuilder<CommandSender, ?>> command, @NotNull CommandSender sender) {
        return cmdInfo(command, sender);
    }
}
