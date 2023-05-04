package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSClaim extends ServerSaturdayCommand {

    @Override
    protected void addToBuilder(LiteralArgumentBuilder<CommandSender> builder) {
        builder.executes(context -> {
            Player player = (Player) context.getSource();
            getRewardHandler().claimReward(player);
            player.sendMessage(Messages.REWARDS_RECEIVED);
            return 1;
        });
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "Claim any pending rewards.";
    }

    @Nonnull
    @Override
    public String getName() {
        return "claim";
    }

    @Nonnull
    @Override
    public String getPermission() {
        return Permissions.SUBMIT;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }
}
