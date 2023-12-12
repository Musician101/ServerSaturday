package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.paper.gui.TextGUI;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    public String getName() {
        return "viewAll";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission(Permissions.FEATURE) && sender instanceof Player;
    }
}
