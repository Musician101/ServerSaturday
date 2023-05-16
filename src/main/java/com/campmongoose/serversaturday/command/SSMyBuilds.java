package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.gui.SubmitterGUI;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SSMyBuilds extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.executes(context -> {
            Player player = (Player) context.getSource();
            new SubmitterGUI(getSubmitter(player), player);
            return 1;
        });
    }

    @NotNull
    @Override
    public String getDescription() {
        return "View your builds.";
    }

    @NotNull
    @Override
    public String getName() {
        return "myBuilds";
    }

    @NotNull
    @Override
    public String getPermission() {
        return Permissions.SUBMIT;
    }
}
