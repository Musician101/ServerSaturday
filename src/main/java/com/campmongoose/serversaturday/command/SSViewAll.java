package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.gui.TextGUI;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSViewAll extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        TextGUI.displayAllSubmissions((Player) context.getSource(), 1);
        return 1;
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss viewAll";
    }

    @Nonnull
    @Override
    public String description() {
        return "View all builds that have been submitted.";
    }

    @Nonnull
    @Override
    public String name() {
        return "viewAll";
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return sender.hasPermission(Permissions.FEATURE) && sender instanceof Player;
    }
}
