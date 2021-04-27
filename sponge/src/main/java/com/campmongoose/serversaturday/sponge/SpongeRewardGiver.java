package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import io.musician101.musicianlibrary.java.configurate.ConfigurateLoader;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.network.ServerSideConnectionEvent.Join;
import org.spongepowered.api.scheduler.Task;

public class SpongeRewardGiver extends RewardGiver<Join, ServerPlayer> {

    public SpongeRewardGiver(@Nonnull File file, @Nonnull ConfigurateLoader configurateLoader) {
        super(file, configurateLoader);
        Sponge.eventManager().registerListeners(SpongeServerSaturday.instance().getPluginContainer(), this);
    }

    @Override
    protected void reportError() {
        SpongeServerSaturday.instance().getLogger().error(Messages.failedToReadFile(file));
    }

    @Override
    public void givePlayerReward(@Nonnull ServerPlayer player) {
        UUID uuid = player.uniqueId();
        int amount = rewardsWaiting.getOrDefault(uuid, 0);
        rewardsWaiting.put(uuid, 0);
        SpongeServerSaturday plugin = SpongeServerSaturday.instance();
        IntStream.range(0, amount).forEach(i -> plugin.getConfig().getRewards().forEach(command -> {
            Server server = Sponge.server();
            CommandCause.create();
            try {
                server.commandManager().process(command.replace("@p", player.name()));
            }
            catch (CommandException e) {
                player.sendMessage(Component.text("Failed to give a reward.").color(NamedTextColor.RED));
                SpongeServerSaturday.instance().getLogger().error("Failed to run command.", e);
            }
        }));
    }

    @Override
    public void onJoin(@Nonnull Join event) {
        ServerPlayer player = event.player();
        UUID uuid = player.uniqueId();
        rewardsWaiting.putIfAbsent(uuid, 0);
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            Task task = Task.builder().execute(() -> player.sendMessage(Component.text(Messages.REWARDS_WAITING).color(NamedTextColor.GOLD))).delay(1, TimeUnit.SECONDS).plugin(SpongeServerSaturday.instance().getPluginContainer()).build();
            Sponge.asyncScheduler().submit(task);
        }
    }
}
