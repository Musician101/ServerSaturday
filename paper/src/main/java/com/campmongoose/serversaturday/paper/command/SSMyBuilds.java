package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.paper.gui.TextGUI;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SSMyBuilds extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = (Player) context.getSource();
        //new SubmitterGUI(getSubmitter(player), player);
        TextGUI.displaySubmitter(player, getSubmitter(player), 1);
        return 1;
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss myBuilds";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "View your builds.";
    }

    @NotNull
    @Override
    public String name() {
        return "myBuilds";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }
}
