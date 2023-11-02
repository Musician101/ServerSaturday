package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.gui.EditBuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSEdit extends ServerSaturdayCommand implements LiteralCommand {

    @NotNull
    @Override
    public String name() {
        return "edit";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss edit <build>";
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Edit a submitted build.";
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSBuild());
    }

    static class SSBuild extends com.campmongoose.serversaturday.command.SSBuild {

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmitter(player);
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(submitter.getUUID());
            if (build == null) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            new EditBuildGUI(build, submitter, player);
            return 1;
        }
    }
}
