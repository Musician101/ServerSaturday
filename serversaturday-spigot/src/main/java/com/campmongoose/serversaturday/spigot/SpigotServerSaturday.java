package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.ServerSaturday;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSCommand;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSFeature;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSGiveReward;
import com.campmongoose.serversaturday.spigot.command.sscommand.SSReload;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSDescription;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSEdit;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSLocation;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSNew;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRemove;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSRename;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSResourcePack;
import com.campmongoose.serversaturday.spigot.command.sscommand.submit.SSSubmit;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSGoto;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSView;
import com.campmongoose.serversaturday.spigot.command.sscommand.view.SSViewDescription;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.spigot.textinput.SpigotTextInput;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotServerSaturday extends JavaPlugin implements
        ServerSaturday<SpigotBuild, ItemStack, PlayerJoinEvent, Location, Player, SpigotRewardGiver, SpigotSubmissions, String, SpigotSubmitter> {

    private SpigotConfig config;
    private SpigotRewardGiver rewardGiver;
    private SpigotSubmissions submissions;

    public static SpigotServerSaturday instance() {
        return JavaPlugin.getPlugin(SpigotServerSaturday.class);
    }

    @Nonnull
    @Override
    public String getId() {
        return Reference.ID;
    }

    @Nonnull
    public SpigotConfig getPluginConfig() {
        return config;
    }

    @Nonnull
    @Override
    public SpigotRewardGiver getRewardGiver() {
        return rewardGiver;
    }

    @Nonnull
    @Override
    public SpigotSubmissions getSubmissions() {
        return submissions;
    }

    @Override
    public void onDisable() {
        getLogger().info(Messages.SAVING_SUBMISSIONS);
        submissions.save();
        getLogger().info(Messages.SUBMISSIONS_SAVED);
    }

    @Override
    public void onEnable() {
        getLogger().info(Messages.LOADING_CONFIG);
        config = new SpigotConfig();
        getLogger().info(Messages.CONFIG_LOADED);
        getLogger().info(Messages.LOADING_SUBMISSIONS);
        submissions = new SpigotSubmissions();
        getLogger().info(Messages.SUBMISSIONS_LOADED);
        getServer().getPluginManager().registerEvents(new SpigotTextInput(), this);
        rewardGiver = new SpigotRewardGiver();
        Server server = getServer();
        String commandPrefix = Reference.NAME.replace(" ", "").toLowerCase();
        server.getPluginCommand(commandPrefix).setExecutor(new SSCommand());
        Stream.of(new SSDescription(), new SSEdit(), new SSFeature(), new SSGiveReward(), new SSGoto(), new SSLocation(),
                new SSNew(), new SSReload(), new SSRemove(), new SSRename(), new SSResourcePack(), new SSSubmit(), new SSView(), new SSViewDescription())
                .forEach(command -> server.getPluginCommand(commandPrefix + command.getName()).setExecutor(command));
    }
}
