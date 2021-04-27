package com.campmongoose.serversaturday.forge.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SSView extends ForgeCommand {

    public SSView() {
        super(Commands.VIEW_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        //TODO open gui
        //new SubmittersGUI(submitter, player);
        return SINGLE_SUCCESS;
    }
}
