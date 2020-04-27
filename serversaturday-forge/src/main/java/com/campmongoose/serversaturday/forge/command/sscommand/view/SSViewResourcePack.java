package com.campmongoose.serversaturday.forge.command.sscommand.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.gui.ForgeBookGUI;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSViewResourcePack extends ForgeCommand {

    public SSViewResourcePack() {
        super(Commands.VIEW_RESOURCE_PACK_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ForgeSubmitter submitter = context.getArgument("submitter", ForgeSubmitter.class);
        if (submitter == null) {
            player.sendMessage(new StringTextComponent(Messages.PLAYER_NOT_FOUND).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        String name = context.getArgument(Commands.BUILD, String.class);
        if (name == null || name.isEmpty()) {
            player.sendMessage(new StringTextComponent(Messages.NOT_ENOUGH_ARGS).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        ForgeBuild build = submitter.getBuild(name);
        if (build == null) {
            player.sendMessage(new StringTextComponent(Messages.BUILD_NOT_FOUND).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        ForgeBookGUI.openWrittenBook(player, build, submitter);
        return SINGLE_SUCCESS;
    }
}
