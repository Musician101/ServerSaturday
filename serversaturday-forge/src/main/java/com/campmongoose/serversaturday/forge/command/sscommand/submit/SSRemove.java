package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSRemove extends ForgeCommand {

    public SSRemove() {
        super(Commands.REMOVE_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String name = context.getArgument(Commands.BUILD, String.class);
        if (name == null || name.isEmpty()) {
            player.sendMessage(new StringTextComponent(Messages.NOT_ENOUGH_ARGS).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        ForgeSubmitter submitter = getSubmitter(player);
        if (submitter.getBuild(name) == null) {
            player.sendMessage(new StringTextComponent(Messages.BUILD_NOT_FOUND).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        submitter.removeBuild(name);
        //TODO open GUI
        //new SubmitterGUI(submitter, player);
        return SINGLE_SUCCESS;
    }
}
