package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import static io.musician101.musigui.sponge.chest.SpongeIconUtil.customName;
import static io.musician101.musigui.sponge.chest.SpongeIconUtil.setLore;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@NotNull Build build, @NotNull Submitter submitter, @NotNull ServerPlayer player) {
        super(build, submitter, 3, 0, player);
        setLeftClickButton(1, setLore(customName(ItemStack.of(ItemTypes.BOOK), MenuText.DESCRIPTION_NAME), MenuText.DESCRIPTION_DESC), p -> handleText(p, build.getDescription(), submitter, build));
        setLeftClickButton(2, setLore(customName(ItemStack.of(ItemTypes.PAINTING), MenuText.RESOURCE_PACK_NAME), MenuText.RESOURCE_PACK_DESC), p -> handleText(p, build.getResourcePack(), submitter, build));
    }

    private void handleText(ServerPlayer player, String string, Submitter submitter, Build build) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + string, GOLD));
        player.sendMessage(text(Messages.PREFIX + "Click here to find reopen the GUI.", GREEN).clickEvent(ClickEvent.runCommand("/ss view " + SpongeSubmissionsUtil.getSubmitterName(submitter) + " " + build.getName())));
    }
}
