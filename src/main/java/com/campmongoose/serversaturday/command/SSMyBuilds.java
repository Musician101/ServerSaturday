package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.gui.TextGUI;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSMyBuilds extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        Player player = (Player) context.getSource();
        //new SubmitterGUI(getSubmitter(player), player);
        TextGUI.displaySubmitter(player, getSubmitter(player), 1);
        return 1;
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss myBuilds";
    }

    @Nonnull
    @Override
    public String description() {
        return "View your builds.";
    }

    @Nonnull
    @Override
    public String name() {
        return "myBuilds";
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canUseSubmit(sender);
    }
}
