package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.command.argument.SubmitterArgumentType;
import com.campmongoose.serversaturday.gui.EditBuildGUI;
import com.campmongoose.serversaturday.gui.SubmitterGUI;
import com.campmongoose.serversaturday.gui.SubmittersGUI;
import com.campmongoose.serversaturday.gui.ViewBuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.bukkitier.Bukkitier.argument;

public class SSView extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.executes(context -> {
            new SubmittersGUI((Player) context.getSource());
            return 1;
        }).then(argument(Commands.PLAYER, new SubmitterArgumentType()).executes(context -> {
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            new SubmitterGUI(submitter, (Player) context.getSource());
            return 1;
        }).then(argument(Commands.BUILD, new BuildArgumentType()).executes(context -> {
            Player player = (Player) context.getSource();
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(submitter.getUUID());
            if (build == null) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            if (player.getUniqueId().equals(submitter.getUUID())) {
                new EditBuildGUI(build, submitter, player);
            }
            else {
                new ViewBuildGUI(build, submitter, player);
            }

            return 1;
        })));
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "View Server Saturday submissions.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "view";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.VIEW;
    }

    @Nonnull
    @Override
    public String getUsage() {
        return "[player] [build]";
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }
}