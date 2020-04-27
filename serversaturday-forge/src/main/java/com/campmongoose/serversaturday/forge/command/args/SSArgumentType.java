package com.campmongoose.serversaturday.forge.command.args;

import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmissions;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.ArgumentType;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public abstract class SSArgumentType<T> implements ArgumentType<T> {

    @Nonnull
    protected ForgeSubmissions getSubmissions() {
        return ForgeServerSaturday.getInstance().getSubmissions();
    }

    @Nonnull
    protected Optional<ForgeSubmitter> getSubmitter(@Nonnull String playerName) {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        GameProfile gp = server.getPlayerProfileCache().getGameProfileForUsername(playerName);
        if (gp == null) {
            return getSubmissions().getSubmitters().stream().filter(s -> playerName.equalsIgnoreCase(s.getName())).findFirst();
        }

        return Optional.ofNullable(getSubmissions().getSubmitter(gp.getId()));
    }

    @Nonnull
    protected Optional<ForgeSubmitter> getSubmitter(@Nonnull UUID uuid) {
        return Optional.ofNullable(getSubmissions().getSubmitter(uuid));
    }

    @Nonnull
    protected ForgeSubmitter getSubmitter(@Nonnull ServerPlayerEntity player) {
        return getSubmissions().getSubmitter(player);
    }
}
