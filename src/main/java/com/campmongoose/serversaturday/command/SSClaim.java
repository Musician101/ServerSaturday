package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public class SSClaim extends ServerSaturdayCommand implements LiteralCommand {

    @Override
    public int execute(@NotNull CommandContext<CommandSender> context) {
        Player player = (Player) context.getSource();
        getRewardHandler().claimReward(player);
        player.sendMessage(text(PREFIX + "All rewards have been given to you.", GOLD));
        return 1;
    }

    @NotNull
    @Override
    public String description(@NotNull CommandSender sender) {
        return "Claim any pending rewards.";
    }

    @NotNull
    @Override
    public String name() {
        return "claim";
    }

    @NotNull
    @Override
    public String usage(@NotNull CommandSender sender) {
        return "/ss claim";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return canUseSubmit(sender);
    }
}
