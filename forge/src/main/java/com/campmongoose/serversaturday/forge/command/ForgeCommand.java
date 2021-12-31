package com.campmongoose.serversaturday.forge.command;

import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import io.musician101.musicianlibrary.java.storage.DataStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
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
        return new StringTextComponent(description).setStyle(Style.EMPTY.setFormatting(TextFormatting.AQUA));
    }

    @Nonnull
    protected ForgeServerSaturday getPluginInstance() {
        return ForgeServerSaturday.getInstance();
    }

    @Nonnull
    protected DataStorage<?, Submitter<TextComponent>> getSubmissions() {
        return getPluginInstance().getSubmissions();
    }

    @Nonnull
    protected Submitter<TextComponent> getSubmitter(@Nonnull ServerPlayerEntity player) {
        DataStorage<?, Submitter<TextComponent>> submissions = getSubmissions();
        Optional<Submitter<TextComponent>> optional =  submissions.getEntry(s -> s.getUUID().equals(player.getUniqueID()));
        if (optional.isPresent()) {
            return optional.get();
        }

        Submitter<TextComponent> submitter = new Submitter<>(player.getName().getUnformattedComponentText(), player.getUniqueID());
        submissions.addEntry(submitter);
        return submitter;
    }

    @Nonnull
    protected Optional<Submitter<TextComponent>> getSubmitter(@Nonnull String playerName) {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        GameProfile gp = server.getPlayerProfileCache().getGameProfileForUsername(playerName);
        if (gp == null) {
            return getSubmissions().getData().stream().filter(s -> playerName.equalsIgnoreCase(s.getName())).findFirst();
        }

        return getSubmissions().getEntry(s -> s.getUUID().equals(gp.getId()));
    }

    protected List<String> moveArguments(List<String> args) {
        List<String> list = new ArrayList<>(args);
        if (!list.isEmpty()) {
            list.remove(0);
        }

        return list;
    }
}
