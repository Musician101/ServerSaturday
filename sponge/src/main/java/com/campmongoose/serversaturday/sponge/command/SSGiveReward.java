package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import javax.annotation.Nonnull;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.User;

public class SSGiveReward extends SSCommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandContext context) {
        User user = context.requireOne(Parameter.key("player", User.class));
        getPlugin().getRewardGiver().addReward(user.uniqueId());
        context.sendMessage(Identity.nil(), Component.text(Messages.rewardsGiven(user.name())).color(NamedTextColor.GOLD));
        user.player().ifPresent(player -> player.sendMessage(Component.text(Messages.REWARDS_WAITING).color(NamedTextColor.GOLD)));
        return CommandResult.success();
    }
}
