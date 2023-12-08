package com.campmongoose.serversaturday.paper.command;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.paper.gui.BuildGUI;
import com.campmongoose.serversaturday.paper.gui.TextGUI;
import com.campmongoose.serversaturday.paper.command.argument.SubmitterArgumentType;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SSView extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        //new SubmittersGUI((Player) context.getSource());
        TextGUI.displaySubmitters((Player) context.getSource(), 1);
        return 1;
    }

    @NotNull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSSubmitter());
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "View a player's submission(s).";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss view [player] [build]";
    }

    @NotNull
    @Override
    public String name() {
        return "view";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player && sender.hasPermission(Permissions.VIEW);
    }

    static class SSSubmitter extends ServerSaturdayCommand implements ArgumentCommand<Submitter> {

        @NotNull
        @Override
        public String name() {
            return Commands.PLAYER;
        }

        @NotNull
        @Override
        public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
            return List.of(new SSViewBuild());
        }

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            //new SubmitterGUI(submitter, (Player) context.getSource());
            TextGUI.displaySubmitter((Player) context.getSource(), submitter, 1);
            return 1;
        }

        @NotNull
        @Override
        public ArgumentType<Submitter> type() {
            return new SubmitterArgumentType();
        }
    }

    public static class SSViewBuild extends SSBuild {

        @Override
        public int execute(@NotNull CommandContext<CommandSender> context) {
            Player player = (Player) context.getSource();
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            Build build = (Build) context.getArgument(Commands.BUILD, Map.class).get(submitter.getUUID());
            if (build == null) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            BuildGUI.open(build, submitter, player);
            return 1;
        }
    }
}
