package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSClaim extends ServerSaturdayCommand implements LiteralCommand{

    @Override
    public int execute(@Nonnull CommandContext<CommandSender> context) {
        Player player = (Player) context.getSource();
        getRewardHandler().claimReward(player);
        player.sendMessage(Messages.REWARDS_RECEIVED);
        return 1;
    }

    @Nonnull
    @Override
    public String description() {
        return "Claim any pending rewards.";
    }

    @Nonnull
    @Override
    public String name() {
        return "claim";
    }

    @Nonnull
    @Override
    public String usage(@Nonnull CommandSender sender) {
        return "/ss claim";
    }

    @Override
    public boolean canUse(@Nonnull CommandSender sender) {
        return canUseSubmit(sender);
    }
}
