package com.campmongoose.serversaturday.forge.command.sscommand.view;

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

public class SSView extends ForgeCommand {

    public SSView() {
        super(Commands.VIEW_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ForgeSubmitter submitter = context.getArgument("submitter", ForgeSubmitter.class);
        if (submitter == null) {
            //TODO open gui
            //new SubmittersGUI(player);
            return SINGLE_SUCCESS;
        }

        String name = context.getArgument(Commands.BUILD, String.class);
        if (name != null && !name.isEmpty()) {
            ForgeBuild build = submitter.getBuild(name);
            if (build == null) {
                player.sendMessage(new StringTextComponent(Messages.BUILD_NOT_FOUND).applyTextStyle(TextFormatting.RED));
                return 0;
            }

            if (submitter.getUUID().equals(player.getUniqueID())) {
                //TODO open gui
                //new EditBuildGUI(build, submitter, player);
            }
            else {
                //TODO open gui
                //new ViewBuildGUI(build, submitter, player);
            }

            return SINGLE_SUCCESS;
        }

        //TODO open gui
        //new SubmitterGUI(submitter, player);
        return SINGLE_SUCCESS;
    }
}
