package com.campmongoose.serversaturday.forge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.forge.command.ForgeCommands;
import com.campmongoose.serversaturday.forge.network.SSNetwork;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmissions;
import javax.annotation.Nonnull;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.ID)
public class ForgeServerSaturday implements ServerSaturday<ForgeRewardGiver, ForgeSubmissions> {

    public static final Logger LOGGER = LogManager.getLogger();

    private ForgeConfig config;
    private ForgeRewardGiver rewardGiver;
    private ForgeSubmissions submissions;

    public ForgeServerSaturday() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ForgeServerSaturday getInstance() {
        return ModList.get().<ForgeServerSaturday>getModObjectById(Reference.ID).orElseThrow(() -> new IllegalStateException("ServerSaturday is not enabled!"));
    }

    private void preInit(FMLCommonSetupEvent event) {
        SSNetwork.init();
    }

    @Nonnull
    public ForgeConfig getConfig() {
        return config;
    }

    @Nonnull
    @Override
    public ForgeRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
    public ForgeSubmissions getSubmissions() {
        return submissions;
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info(Messages.LOADING_CONFIG);
        config = new ForgeConfig();
        LOGGER.info(Messages.CONFIG_LOADED);
        LOGGER.info(Messages.LOADING_SUBMISSIONS);
        submissions = new ForgeSubmissions();
        LOGGER.info(Messages.SUBMISSIONS_LOADED);
        rewardGiver = new ForgeRewardGiver();
        ForgeCommands.init();
    }
}
