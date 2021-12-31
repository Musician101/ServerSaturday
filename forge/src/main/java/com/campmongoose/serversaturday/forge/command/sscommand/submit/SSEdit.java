package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextComponent;

public class SSEdit extends ForgeCommand {

    public SSEdit() {
        super(Commands.EDIT_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Submitter<TextComponent> submitter = getSubmitter(player);

        //TODO open GUI
        //new SubmitterGUI(submitter, player);
        return SINGLE_SUCCESS;
    }
}
