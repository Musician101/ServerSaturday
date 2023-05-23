package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.command.argument.SubmitterArgumentType;
import com.campmongoose.serversaturday.gui.BuildGUI;
import com.campmongoose.serversaturday.gui.TextGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.ArgumentCommand;
import io.musician101.bukkitier.command.Command;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSView extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        //new SubmittersGUI((Player) context.getSource());
        TextGUI.displaySubmitters((Player) context.getSource(), 1);
        return 1;
    }

    @Nonnull
    @Override
    public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
        return List.of(new SSSubmitter());
    }

    @Nonnull
    @Override
    public String description() {
        return "View a player's submission(s).";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss view [player] [build]";
    }

    @Nonnull
    @Override
    public String name() {
        return "view";
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return sender instanceof Player && sender.hasPermission(Permissions.VIEW);
    }

    static class SSSubmitter extends ServerSaturdayCommand implements ArgumentCommand<Submitter> {

        @Nonnull
        @Override
        public String name() {
            return Commands.PLAYER;
        }

        @Nonnull
        @Override
        public List<Command<? extends ArgumentBuilder<CommandSender, ?>>> arguments() {
            return List.of(new SSViewBuild());
        }

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
            Submitter submitter = context.getArgument(Commands.PLAYER, Submitter.class);
            //new SubmitterGUI(submitter, (Player) context.getSource());
            TextGUI.displaySubmitter((Player) context.getSource(), submitter, 1);
            return 1;
        }

        @Nonnull
        @Override
        public ArgumentType<Submitter> type() {
            return new SubmitterArgumentType();
        }
    }

    public static class SSViewBuild extends SSBuild {

        @Override
        public int execute(@Nonnull CommandContext<CommandSender> context) {
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
