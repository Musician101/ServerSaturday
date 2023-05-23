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
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSEdit extends ServerSaturdayCommand implements LiteralCommand {

    @Nonnull
    @Override
    public String name() {
        return "edit";
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canUseSubmit(sender);
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss edit <build>";
    }

    @Nonnull
    @Override
    public String description() {
        return "Edit a submitted build.";
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSBuild());
    }

    static class SSBuild extends com.campmongoose.serversaturday.command.SSBuild {

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
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
