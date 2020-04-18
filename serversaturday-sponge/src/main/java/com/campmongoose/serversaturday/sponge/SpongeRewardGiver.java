package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SpongeRewardGiver extends RewardGiver<ClientConnectionEvent.Join, Player> {

    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public SpongeRewardGiver(File file) {
        super(file);
        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }

            loader = HoconConfigurationLoader.builder().setFile(this.file).build();
            ConfigurationNode node = loader.load();
            node.getChildrenMap().forEach((key, data) -> {
                UUID uuid = UUID.fromString(key.toString());
                rewardsWaiting.put(uuid, data.getNode("amount").getInt(0));
            });
        }
        catch (IOException e) {
            SpongeServerSaturday.instance().getLogger().error(Messages.failedToReadFile(this.file));
        }
    }

    @Override
    public void givePlayerReward(@Nonnull Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewardsWaiting.getOrDefault(uuid, 0);
        rewardsWaiting.put(uuid, 0);
        SpongeServerSaturday plugin = SpongeServerSaturday.instance();
        IntStream.range(0, amount).forEach(i -> plugin.getConfig().getRewards().forEach(command -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command.replace("@p", player.getName()))));
    }

    @Override
    public void onJoin(@Nonnull Join event) {
        Player player = event.getTargetEntity();
        UUID uuid = player.getUniqueId();
        rewardsWaiting.putIfAbsent(uuid, 0);
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            Task.builder().execute(() -> player.sendMessage(Text.of(TextColors.GOLD, Messages.REWARDS_WAITING))).delay(1, TimeUnit.SECONDS).submit(SpongeServerSaturday.instance());
        }
    }

    @Override
    public void save() {
        ConfigurationNode node = loader.createEmptyNode();
        rewardsWaiting.forEach((uuid, amount) -> {
            node.getNode(uuid.toString(), "amount").setValue(amount);
            Sponge.getServiceManager().provide(UserStorageService.class).flatMap(userStorage -> userStorage.get(uuid)).map(User::getName).ifPresent(name -> node.getNode(uuid.toString(), "name").setValue(name));
        });

        try {
            loader.save(node);
        }
        catch (IOException e) {
            SpongeServerSaturday.instance().getLogger().error(Messages.failedToWriteFile(file));
        }
    }
}
