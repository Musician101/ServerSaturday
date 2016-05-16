package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.command.AbstractCommandPermissions;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SpongeCommandPermissions extends AbstractCommandPermissions<Text>
{
    public SpongeCommandPermissions(String permissionNode, boolean isPlayerOnly)
    {
        super(permissionNode, isPlayerOnly, Text.builder(Messages.NO_PERMISSION).color(TextColors.RED).build(), Text.builder(Messages.PLAYER_ONLY).color(TextColors.RED).build());
    }
}
