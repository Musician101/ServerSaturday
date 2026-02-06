package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Messages;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
public class SSDelete implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new BuildArgument());
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return canUseSubmit(sender.getSender());
    }

    @Override
    public Component description(CommandSourceStack sender) {
        return Component.text("Delete a submission.");
    }

    @Override
    public String name() {
        return "delete";
    }

    @Override
    public Component usage(CommandSourceStack sender) {
        return Component.text("/ss delete <build>");
    }

    static class BuildArgument extends SSBuild {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            Player player = (Player) context.getSource();
            Submitter submitter = getSubmitter(player);
            Optional<Build> build = context.getArgument(name(), Holder.class).get(submitter);
            if (build.isEmpty()) {
                player.sendMessage(Messages.BUILD_DOES_NOT_EXIST);
                return 0;
            }

            submitter.builds().remove(build.get());
            player.sendMessage(text(Messages.PREFIX + "Build deleted.", GREEN));
            return 1;
        }
    }
}
