package com.campmongoose.serversaturday.sponge.command.submit;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSGetRewards extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) {
        if (src instanceof Player) {
            getPlugin().getRewardGiver().givePlayerReward((Player) src);
            src.sendMessage(Text.of(TextColors.GOLD, Messages.REWARDS_RECEIVED));
            return CommandResult.success();
        }

        src.sendMessage(Text.of(TextColors.RED, Messages.PLAYER_NOT_FOUND));
        return CommandResult.empty();
    }
}
