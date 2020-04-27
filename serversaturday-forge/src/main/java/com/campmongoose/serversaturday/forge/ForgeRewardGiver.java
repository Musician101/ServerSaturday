package com.campmongoose.serversaturday.forge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Config;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.RewardGiver;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class ForgeRewardGiver extends RewardGiver<PlayerLoggedInEvent, ServerPlayerEntity> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ForgeRewardGiver() {
        super(new File("config/" + Reference.ID, "rewards_waiting.json"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                ForgeServerSaturday.LOGGER.warn(Messages.failedToReadFile(file));
                return;
            }
        }

        try (FileReader fr = new FileReader(file)) {
            JsonObject jsonObject = GSON.fromJson(fr, JsonObject.class);
            jsonObject.entrySet().forEach(entry -> {
                UUID uuid = UUID.fromString(entry.getKey());
                rewardsWaiting.put(uuid, entry.getValue().getAsJsonObject().get("amount").getAsInt());
            });
        }
        catch (IOException e) {
            ForgeServerSaturday.LOGGER.warn(Messages.failedToReadFile(file));
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void givePlayerReward(@Nonnull ServerPlayerEntity player) {
        UUID uuid = player.getUniqueID();
        int amount = rewardsWaiting.getOrDefault(uuid, 0);
        rewardsWaiting.put(uuid, 0);
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        IntStream.range(0, amount).forEach(i -> ForgeServerSaturday.getInstance().getConfig().getRewards().forEach(command -> server.getCommandManager().handleCommand(server.getCommandSource(), command)));
    }

    @SubscribeEvent
    @Override
    public void onJoin(@Nonnull PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID uuid = player.getUniqueID();
        if (rewardsWaiting.getOrDefault(uuid, 0) > 0) {
            MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
            server.enqueue(new TickDelayedTask(server.getTickCounter() + 20, () -> player.sendMessage(new StringTextComponent(Messages.REWARDS_WAITING).applyTextStyle(TextFormatting.GOLD))));
        }
    }

    @Override
    public void save() {
        try (FileWriter fw = new FileWriter(file)) {
            JsonObject main = new JsonObject();
            rewardsWaiting.forEach((uuid, amount) -> {
                JsonObject user = new JsonObject();
                user.addProperty("amount", amount);
                ForgeSubmitter submitter = ForgeServerSaturday.getInstance().getSubmissions().getSubmitter(uuid);
                user.addProperty(Config.NAME, submitter == null ? "Invalid UUID?" : submitter.getName());
                main.add(uuid.toString(), user);
            });

            GSON.toJson(main, fw);
        }
        catch (IOException e) {
            ForgeServerSaturday.LOGGER.warn(Messages.failedToWriteFile(file));
        }
    }
}
