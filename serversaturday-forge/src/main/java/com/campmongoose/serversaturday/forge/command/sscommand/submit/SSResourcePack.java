package com.campmongoose.serversaturday.forge.command.sscommand.submit;

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
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSResourcePack extends ForgeCommand {

    public SSResourcePack() {
        super(Commands.RESOURCE_PACK_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (!itemStack.isEmpty()) {
            player.sendMessage(new StringTextComponent(Messages.HAND_NOT_EMPTY).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        if (ForgeBookGUI.isEditing(player)) {
            player.sendMessage(new StringTextComponent(Messages.EDIT_IN_PROGRESS).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        String name = context.getArgument(Commands.BUILD, String.class);
        ForgeSubmitter submitter = getSubmitter(player);
        ForgeBuild build = submitter.getBuild(name);
        if (build == null) {
            player.sendMessage(new StringTextComponent(Messages.BUILD_NOT_FOUND).applyTextStyle(TextFormatting.RED));
            return 0;
        }

        new ForgeBookGUI(player, build, build.getResourcePacks(), (ply, pages) -> {
            build.setResourcePacks(pages);
            //TODO open gui
            //new EditBuildGUI(build, submitter, ply);
        });
        return SINGLE_SUCCESS;
    }
}
