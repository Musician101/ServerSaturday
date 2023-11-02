package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.gui.TextGUI;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSViewAll extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        TextGUI.displayAllSubmissions((Player) context.getSource(), 1);
        return 1;
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss viewAll";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "View all builds that have been submitted.";
    }

    @NotNull
    @Override
    public String name() {
        return "viewAll";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission(Permissions.FEATURE) && sender instanceof Player;
    }
}
