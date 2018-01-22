package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.sponge.command.SpongeCommands;
import com.campmongoose.serversaturday.sponge.data.manipulator.builder.InventorySlotDataBuilder;
import com.campmongoose.serversaturday.sponge.data.manipulator.builder.UUIDDataBuilder;
import com.campmongoose.serversaturday.sponge.data.manipulator.immutable.ImmutableInventorySlotData;
import com.campmongoose.serversaturday.sponge.data.manipulator.immutable.ImmutableUUIDData;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.InventorySlotData;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.UUIDData;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import com.google.inject.Inject;
import java.io.File;
import java.util.Optional;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday implements ServerSaturday<SpongeBuild, ItemStack, ClientConnectionEvent.Join, Logger, Location<World>, Player, SpongeRewardGiver, SpongeSubmissions, SpongeSubmitter> {

    private SpongeConfig config;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer pluginContainer;
    private SpongeRewardGiver rewardGiver;
    private SpongeSubmissions submissions;

    public static Optional<SpongeServerSaturday> instance() {
        return Sponge.getPluginManager().getPlugin(Reference.ID).flatMap(PluginContainer::getInstance).filter(SpongeServerSaturday.class::isInstance).map(SpongeServerSaturday.class::cast);
    }

    public SpongeConfig getConfig() {
        return config;
    }

    @Override
    public String getId() {
        return pluginContainer.getId();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public SpongeRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Override
    public SpongeSubmissions getSubmissions() {
        return submissions;
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        logger.info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        logger.info(Messages.SUBMISSIONS_SAVED);
    }

    @Listener
    public void onEnable(GameStartingServerEvent event) {
        logger.info(Messages.LOADING_CONFIG);
        config = new SpongeConfig(defaultConfig);
        logger.info(Messages.CONFIG_LOADED);
        registerData("InventorySlot", getId() + ":inventory_slot", InventorySlotData.class, ImmutableInventorySlotData.class, new InventorySlotDataBuilder());
        registerData("BookGUIData", getId() + ":book_gui", UUIDData.class, ImmutableUUIDData.class, new UUIDDataBuilder());
        logger.info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpongeSubmissions(defaultConfig.getParentFile());
        logger.info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new SpongeRewardGiver(new File(defaultConfig.getParentFile(), "rewards.conf"));
        SpongeCommands.init(this);
        Sponge.getEventManager().registerListener(this, ClientConnectionEvent.Join.class, e -> {
            Player player = e.getTargetEntity();
            getSubmissions().getSubmitter(player).setName(player.getName());
        });
    }

    private <D extends DataManipulator<D, M>, M extends ImmutableDataManipulator<M, D>> void registerData(String dataName, String id, Class<D> manipulatorClass, Class<M> immutableDataClass, DataManipulatorBuilder<D, M> builder) {
        DataRegistration.builder().dataClass(manipulatorClass).dataName(dataName).manipulatorId(id).immutableClass(immutableDataClass).builder(builder).buildAndRegister(pluginContainer);
    }
}
