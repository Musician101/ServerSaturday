package com.campmongoose.serversaturday.forge.command.sscommand;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SSReload extends ForgeCommand {

    public SSReload() {
        super(Commands.RELOAD_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        getSubmissions().save();
        getSubmissions().load();
        getPluginInstance().getConfig().reload();
        source.sendFeedback(new StringTextComponent(Messages.PLUGIN_RELOADED).applyTextStyle(TextFormatting.GOLD), true);
        return SINGLE_SUCCESS;
    }
}
