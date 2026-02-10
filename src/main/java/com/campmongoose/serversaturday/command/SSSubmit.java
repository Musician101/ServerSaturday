package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.submission.Build;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.core.command.CommandException;
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
public class SSSubmit implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public String name() {
        return "submit";
    }

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new BuildArgument());
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return canUseSubmit(sender.getSender());
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.submit.description");
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss submit <build>");
    }

    static class BuildArgument extends SSBuild {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
            Player player = getPlayer(context);
            Optional<Build> optional = context.getArgument(name(), Holder.class).get(getSubmitter(player));
            if (optional.isEmpty()) {
                player.sendMessage(Component.translatable("ss.command.build-does-not-exist"));
                return 0;
            }

            Build build = optional.get();
            build.submitted(!build.submitted());
            if (build.submitted()) {
                player.sendMessage(Component.translatable("ss.command.submit.submitted"));
            }
            else {
                player.sendMessage(Component.translatable("ss.command.submit.not-submitted"));
            }

            return 1;
        }
    }
}
