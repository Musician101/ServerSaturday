package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.campmongoose.serversaturday.forge.textinput.ForgeTextInput;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSRename extends ForgeCommand {

    public SSRename() {
        super(Commands.RENAME_DESC);
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

        player.sendMessage(new StringTextComponent(Messages.SET_BUILD_NAME).applyTextStyle(TextFormatting.GREEN));
        ForgeTextInput.addPlayer(player, (p, s) -> {
            submitter.renameBuild(s, build);
            //TODO open GUI
            //new EditBuildGUI(build, submitter, p);
        });
        return SINGLE_SUCCESS;
    }
}
