package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

@NullMarked
public class SSClaim implements PaperLiteralCommand.AdventureFormat, SSCommand {

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        Player player = (Player) context.getSource();
        getRewardHandler().claimReward(player);
        player.sendMessage(text(PREFIX + "All rewards have been given to you.", GOLD));
        return 1;
    }

    @Override
    public ComponentLike description(CommandSourceStack sender) {
        return Component.text("Claim any pending rewards.");
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
