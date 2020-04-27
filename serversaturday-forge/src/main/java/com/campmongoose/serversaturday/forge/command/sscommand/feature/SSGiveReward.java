package com.campmongoose.serversaturday.forge.command.sscommand.feature;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSGiveReward extends ForgeCommand {

    public SSGiveReward() {
        super(Commands.GIVE_REWARD_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        GameProfile gameProfile = context.getArgument(Commands.PLAYER, GameProfile.class);
        getPluginInstance().getRewardGiver().addReward(gameProfile.getId());
        source.sendFeedback(new StringTextComponent(Messages.rewardsGiven(gameProfile.getName())).applyTextStyle(TextFormatting.GOLD), true);
        ServerPlayerEntity player = source.getServer().getPlayerList().getPlayerByUUID(gameProfile.getId());
        if (player != null) {
            player.sendMessage(new StringTextComponent(Messages.REWARDS_WAITING).applyTextStyle(TextFormatting.GOLD));
        }

        return SINGLE_SUCCESS;
    }
}
