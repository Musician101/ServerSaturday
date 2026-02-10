package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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

@NullMarked
public class SSNew implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new SSId());
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return canUseSubmit(sender.getSender());
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.new.description");
    }

    @Override
    public String name() {
        return "new";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss new <id>");
    }

    static class SSId implements PaperArgumentCommand.AdventureFormat<String>, SSCommand {

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
            Player player = getPlayer(context);
            String id = StringArgumentType.getString(context, "id");
            Submitter submitter = getSubmitter(player);
            if (submitter.getBuild(id).isPresent()) {
                player.sendMessage(Component.translatable("ss.command.new.fail"));
                return 0;
            }

            getSubmitter(player).newBuild(id, id, player.getLocation());
            player.sendMessage(Component.translatable("ss.command.new.success"));
            return 1;
        }

        @Override
        public String name() {
            return "id";
        }

        @Override
        public ArgumentType<String> type() {
            return StringArgumentType.greedyString();
        }
    }
}
