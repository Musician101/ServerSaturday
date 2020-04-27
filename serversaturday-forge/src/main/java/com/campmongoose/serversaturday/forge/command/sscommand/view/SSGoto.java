package com.campmongoose.serversaturday.forge.command.sscommand.view;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.campmongoose.serversaturday.forge.submission.Location;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSGoto extends ForgeCommand {

    public SSGoto() {
        super(Commands.GOTO_DESC);
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

        Location location = build.getLocation();
        player.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        player.sendMessage(new StringTextComponent(Messages.teleportedToBuild(build)).applyTextStyle(TextFormatting.GOLD));
        return SINGLE_SUCCESS;
    }
}
