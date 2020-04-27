package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSSubmit extends ForgeCommand {

    public SSSubmit() {
        super(Commands.SUBMIT_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String name = context.getArgument(Commands.BUILD, String.class);
        ForgeSubmitter submitter = getSubmitter(player);
        ForgeBuild build = submitter.getBuild(name);
        if (build == null) {
            player.sendMessage(new StringTextComponent(Messages.BUILD_NOT_FOUND).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        build.setSubmitted(!build.submitted());
        //TODO edit gui
        //new EditBuildGUI(build, submitter, player);
        return SINGLE_SUCCESS;
    }
}
