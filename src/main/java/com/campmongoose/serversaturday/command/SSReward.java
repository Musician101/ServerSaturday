package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Messages;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

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
        return Component.text("Give a player a reward.");
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
            sendMessage(context, text(PREFIX + "Rewards given to " + offlinePlayer.getName(), GOLD));
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(Messages.REWARDS_WAITING);
            }

            return 1;
        }

        @Override
        public ArgumentType<OfflinePlayer> type() {
            return new OfflinePlayerArgumentType();
        }
    }
}
