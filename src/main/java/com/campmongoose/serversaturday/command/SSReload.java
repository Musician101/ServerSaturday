package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class SSReload extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        getSubmissions().load();
        getPlugin().reload();
        context.getSource().sendMessage(text(PREFIX + "Plugin reloaded. Check console for errors.", GOLD));
        return 1;
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss reload";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Reload the plugin.";
    }

    @NotNull
    @Override
    public String name() {
        return "reload";
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("ss.admin");
    }
}
