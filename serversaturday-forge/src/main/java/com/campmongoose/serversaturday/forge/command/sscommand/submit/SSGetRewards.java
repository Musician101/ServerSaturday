package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSGetRewards extends ForgeCommand {

    public SSGetRewards() {
        super(Commands.GET_REWARDS_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        getPluginInstance().getRewardGiver().givePlayerReward(source.asPlayer());
        source.sendFeedback(new StringTextComponent(Messages.REWARDS_RECEIVED).applyTextStyle(TextFormatting.GOLD), true);
        return SINGLE_SUCCESS;
    }
}
