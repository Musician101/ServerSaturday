package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.RewardHandler;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.core.command.CommandException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
public interface SSCommand {

    String BUILD = "build";
    String PLAYER = "player";

    default Player getPlayer(CommandContext<CommandSourceStack> context) throws CommandException {
        CommandSender sender = context.getSource().getSender();
        if (sender instanceof Player player) {
            return player;
        }

        throw new CommandException("Tried to get player when sender is " + sender.getName());
    }

    default RewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    default Submissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    default Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player);
    }

    default boolean canUseSubmit(CommandSender sender) {
        return sender instanceof Player && sender.hasPermission("ss.submit");
    }

    default void sendMessage(CommandContext<CommandSourceStack> context, Component message) {
        context.getSource().getSender().sendMessage(message);
    }
}
