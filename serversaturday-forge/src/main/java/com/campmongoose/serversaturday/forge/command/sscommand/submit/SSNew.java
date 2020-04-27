package com.campmongoose.serversaturday.forge.command.sscommand.submit;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.campmongoose.serversaturday.forge.submission.Location;
import com.campmongoose.serversaturday.forge.textinput.ForgeTextInput;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSNew extends ForgeCommand {

    public SSNew() {
        super(Commands.NEW_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        if (getPluginInstance().getConfig().getMaxBuilds() > 0 && !player.hasPermissionLevel(3)) {
            player.sendMessage(new StringTextComponent(Messages.NO_PERMISSION).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        ForgeSubmitter submitter = getSubmitter(player);
        String name = context.getArgument(Commands.BUILD, String.class);
        if (name == null || name.isEmpty()) {
            player.sendMessage(new StringTextComponent(Messages.SET_BUILD_NAME).applyTextStyle(TextFormatting.GREEN));
            ForgeTextInput.addPlayer(player, (p, s) -> {
                if (submitter.getBuild(s) != null) {
                    player.sendMessage(new StringTextComponent(MenuText.ALREADY_EXISTS).applyTextStyle(TextFormatting.RED));
                    return;
                }

                ForgeBuild build = submitter.newBuild(s, new Location(player));
                //TODO open GUI
                //new EditBuildGUI(build, submitter, p);
            });

            return SINGLE_SUCCESS;
        }

        if (submitter.getBuild(name) != null) {
            player.sendMessage(new StringTextComponent(Messages.BUILD_ALREADY_EXISTS).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        ForgeBuild build = submitter.newBuild(name, new Location(player));
        //TODO open GUI
        //new EditBuildGUI(build, submitter, player);
        return SINGLE_SUCCESS;
    }
}
