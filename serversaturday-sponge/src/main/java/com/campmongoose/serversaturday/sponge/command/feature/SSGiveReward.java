package com.campmongoose.serversaturday.sponge.command.feature;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.command.SSCommandExecutor;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SSGiveReward extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource src, CommandContext args) {
        return args.<User>getOne("player").map(user -> {
            getPlugin().getRewardGiver().addReward(user.getUniqueId());
            src.sendMessage(Text.of(TextColors.GOLD, Messages.rewardsGiven(user.getName())));
            user.getPlayer().ifPresent(player -> player.sendMessage(Text.of(TextColors.GOLD, Messages.REWARDS_WAITING)));
            return CommandResult.success();
        }).orElseGet(() -> {
            src.sendMessage(Text.of(TextColors.RED, Messages.PLAYER_NOT_FOUND));
            return CommandResult.empty();
        });
    }
}
