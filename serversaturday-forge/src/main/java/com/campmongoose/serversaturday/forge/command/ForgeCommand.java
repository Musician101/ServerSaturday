package com.campmongoose.serversaturday.forge.command;

import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmissions;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public abstract class ForgeCommand implements Command<CommandSource> {

    @Nonnull
    private final String description;

    protected ForgeCommand(@Nonnull String description) {
        this.description = description;
    }

    @Nonnull
    public ITextComponent getDescription() {
        return new StringTextComponent(description).applyTextStyle(TextFormatting.AQUA);
    }

    @Nonnull
    protected ForgeServerSaturday getPluginInstance() {
        return ForgeServerSaturday.getInstance();
    }

    @Nonnull
    protected ForgeSubmissions getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    protected ForgeSubmitter getSubmitter(@Nonnull ServerPlayerEntity player) {
        return getSubmissions().getSubmitter(player);
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

    protected List<String> moveArguments(List<String> args) {
        List<String> list = new ArrayList<>(args);
        if (!list.isEmpty()) {
            list.remove(0);
        }

        return list;
    }
}
