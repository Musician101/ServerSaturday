package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.sponge.gui.chest.SpongeRewardsGUI;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

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
            SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> logger.error(Messages.failedToReadFile(this.file)));
        }
    }

    @Override
    public void givePlayerReward(Player player) {
        UUID uuid = player.getUniqueId();
        int amount = rewardsWaiting.put(uuid, 0);
        for (int i = 0; i < amount; i++) {
            SpongeRewardsGUI.getRewards().stream().filter(Objects::nonNull).forEach(itemStack -> {
                World world = player.getWorld();
                Entity entity = world.createEntity(EntityTypes.ITEM, player.getLocation().getPosition());
                entity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
                world.spawnEntity(entity);
            });
        }
    }

    @Override
    public void onJoin(Join event) {
        Player player = event.getTargetEntity();
        UUID uuid = player.getUniqueId();
        rewardsWaiting.putIfAbsent(uuid, 0);
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            player.sendMessage(Text.of(TextColors.GOLD, Messages.REWARDS_WAITING));
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
            SpongeServerSaturday.instance().map(ServerSaturday::getLogger).ifPresent(logger -> logger.error(Messages.failedToWriteFile(file)));
        }
    }
}
