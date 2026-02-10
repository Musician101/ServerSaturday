package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.command.argument.SubmitterArgumentType;
import com.campmongoose.serversaturday.dialog.SubmitterDialog;
import com.campmongoose.serversaturday.dialog.SubmittersDialog;
import com.campmongoose.serversaturday.gui.BuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public class SSView implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        context.getSource().getSender().showDialog(new SubmittersDialog().build());
        return 1;
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new SSSubmitter());
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.view.description");
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss view [<player> [<build>]]");
    }

    @Override
    public String name() {
        return "view";
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return sender.getSender() instanceof Player player && player.hasPermission("ss.view");
    }

    static class SSSubmitter implements PaperArgumentCommand.AdventureFormat<Submitter>, SSCommand {

        @Override
        public String name() {
            return PLAYER;
        }

        @Override
        public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
            return List.of(new BuildArgument());
        }

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Submitter submitter = context.getArgument(PLAYER, Submitter.class);
            context.getSource().getSender().showDialog(new SubmitterDialog(submitter).build());
            return 1;
        }

        @Override
        public ArgumentType<Submitter> type() {
            return new SubmitterArgumentType();
        }
    }

    public static class BuildArgument extends SSBuild {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
            Player player = getPlayer(context);
            Submitter submitter = context.getArgument(PLAYER, Submitter.class);
            Optional<Build> build = context.getArgument(name(), Holder.class).get(submitter);
            if (build.isEmpty()) {
                player.sendMessage(Component.translatable("ss.command.build-does-not-exist"));
                return 0;
            }

            BuildGUI.open(build.get(), submitter, player);
            return 1;
        }

        @Override
        public ArgumentType<Holder> type() {
            return BuildArgumentType.VIEWER;
        }
    }
}
