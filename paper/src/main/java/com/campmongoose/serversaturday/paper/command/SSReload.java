package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;

public class SSReload extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        getSubmissions().save();
        getSubmissions().load();
        getPlugin().reloadPluginConfig();
        context.getSource().sendMessage(Messages.PLUGIN_RELOADED);
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
    public String getName() {
        return "reload";
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(Permissions.ADMIN);
    }
}
