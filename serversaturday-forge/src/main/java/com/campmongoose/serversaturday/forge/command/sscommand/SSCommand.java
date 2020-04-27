package com.campmongoose.serversaturday.forge.command.sscommand;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class SSCommand extends ForgeCommand {

    public SSCommand() {
        super(Commands.HELP_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        ITextComponent message = new StringTextComponent("===== ").applyTextStyle(TextFormatting.GREEN).appendSibling(new StringTextComponent(Reference.NAME + " v" + Reference.VERSION).applyTextStyle(TextFormatting.RESET)).appendSibling(new StringTextComponent(" by ").applyTextStyle(TextFormatting.GREEN)).appendSibling(new StringTextComponent("Musician101").applyTextStyle(TextFormatting.RESET)).appendSibling(new StringTextComponent(" =====").applyTextStyle(TextFormatting.GREEN));
        source.sendFeedback(message, true);
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        CommandDispatcher<CommandSource> dispatcher = server.getCommandManager().getDispatcher();
        dispatcher.getRoot().getChildren().stream().filter(node -> node.getName().startsWith(Reference.ID.replace("_", "")) || node.getName().startsWith(Commands.SS_CMD.replace("/", ""))).filter(node -> node.canUse(source) && node.getCommand() instanceof ForgeCommand).forEach(node -> source.sendFeedback(new StringTextComponent(node.getUsageText()).appendSibling(((ForgeCommand) node.getCommand()).getDescription()), true));
        return SINGLE_SUCCESS;
    }
}
