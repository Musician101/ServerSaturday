package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSClaim extends ServerSaturdayCommand implements LiteralCommand{

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = (Player) context.getSource();
        getRewardHandler().claimReward(player);
        player.sendMessage(Messages.REWARDS_RECEIVED);
        return 1;
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Claim any pending rewards.";
    }

    @NotNull
    @Override
    public String name() {
        return "claim";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss claim";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }
}
