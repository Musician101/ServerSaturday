package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.core.command.CommandException;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SSClaim implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) throws CommandException {
        Player player = getPlayer(context);
        getRewardHandler().claimReward(player);
        player.sendMessage(Component.translatable("ss.command.claim.success"));
        return 1;
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.translatable("ss.command.claim.description");
    }

    @Override
    public String name() {
        return "claim";
    }

    @Override
    public ComponentLike usage(CommandSourceStack source) {
        return Component.text("/ss claim");
    }

    @Override
    public boolean canUse(CommandSourceStack sender) {
        return canUseSubmit(sender.getSender());
    }
}
