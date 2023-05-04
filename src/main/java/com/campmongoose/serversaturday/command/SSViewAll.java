package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.gui.AllSubmissionsGUI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSViewAll extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.executes(context -> {
            new AllSubmissionsGUI((Player) context.getSource());
            return 1;
        });
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "View all builds that have been submitted.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "viewAll";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.FEATURE;
    }
}
