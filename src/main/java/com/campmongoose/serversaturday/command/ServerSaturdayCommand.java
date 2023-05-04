package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.RewardHandler;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submissions;
import com.campmongoose.serversaturday.submission.Submitter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javax.annotation.Nonnull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.musician101.bukkitier.Bukkitier.literal;

public abstract class ServerSaturdayCommand {

    protected abstract void addToBuilder(LiteralArgumentBuilder<CommandSender> builder);

    @Nonnull
    public abstract String getDescription();

    @Nonnull
    public abstract String getName();

    @Nonnull
    public abstract String getPermission();

    protected final ServerSaturday getPlugin() {
        return ServerSaturday.getInstance();
    }

    protected final RewardHandler getRewardHandler() {
        return getPlugin().getRewardHandler();
    }

    protected final Submissions getSubmissions() {
        return getPlugin().getSubmissions();
    }

    protected final Submitter getSubmitter(Player player) {
        return getSubmissions().getSubmitter(player);
    }

    @Nonnull
    public String getUsage() {
        return "";
    }

    protected boolean isPlayerOnly() {
        return false;
    }

    @Nonnull
    public final LiteralArgumentBuilder<CommandSender> toBukkitier() {
        LiteralArgumentBuilder<CommandSender> builder = literal(getName()).requires(sender -> {
            if (isPlayerOnly()) {
                return sender instanceof Player;
            }

            return sender.hasPermission(getPermission());
        });
        addToBuilder(builder);
        return builder;
    }
}
