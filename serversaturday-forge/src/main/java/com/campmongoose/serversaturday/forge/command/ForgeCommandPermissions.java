package com.campmongoose.serversaturday.forge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ForgeCommandPermissions {

    private final boolean isPlayerOnly;
    private final int permissionLevel;

    public ForgeCommandPermissions(int permissionLevel, boolean isPlayerOnly) {
        this.permissionLevel = permissionLevel;
        this.isPlayerOnly = isPlayerOnly;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public boolean testPermission(CommandSource source) {
        try {
            ServerPlayerEntity player = source.asPlayer();
            if (!player.hasPermissionLevel(permissionLevel)) {
                source.sendFeedback(ChatColor.RED + Messages.NO_PERMISSION);
                return false;
            }
        }
        catch (CommandSyntaxException e) {
            if (isPlayerOnly) {
                source.sendFeedback(new StringTextComponent(Messages.PLAYER_ONLY).applyTextStyle(TextFormatting.RED), true);
                return false;
            }
        }

        return true;
    }
}
