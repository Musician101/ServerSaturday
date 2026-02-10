package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.OfflinePlayerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import io.musician101.musicommand.paper.command.PaperCommand;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class SSReward implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public List<PaperCommand<? extends ArgumentBuilder<CommandSourceStack, ?>, ComponentLike>> children() {
        return List.of(new SSPlayer());
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss reward <player>");
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.reward.description");
    }

    @Override
    public String name() {
        return "reward";
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return sender.getSender().hasPermission("ss.feature");
    }

    static class SSPlayer implements PaperArgumentCommand.AdventureFormat<OfflinePlayer>, SSCommand {

        @Override
        public String name() {
            return PLAYER;
        }

        @Override
        public Integer execute(CommandContext<CommandSourceStack> context) {
            OfflinePlayer offlinePlayer = context.getArgument(PLAYER, OfflinePlayer.class);
            getRewardHandler().giveReward(offlinePlayer);
            String playerName = offlinePlayer.getName();
            if (playerName == null) {
                playerName = offlinePlayer.getUniqueId().toString();
            }

            ComponentLike argument = Argument.tagResolver(Placeholder.unparsed("player", playerName));
            sendMessage(context, Component.translatable("ss.command.reward.success", argument));
            return 1;
        }

        @Override
        public ArgumentType<OfflinePlayer> type() {
            return new OfflinePlayerArgumentType();
        }
    }
}
