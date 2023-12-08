package com.campmongoose.serversaturday.paper.gui;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissionsUtil;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.musician101.musigui.paper.chest.PaperIconUtil.customName;
import static io.musician101.musigui.paper.chest.PaperIconUtil.setLore;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@NotNull Build build, @NotNull Submitter submitter, @NotNull Player player) {
        super(build, submitter, 3, 0, player);
        setLeftClickButton(1, setLore(customName(new ItemStack(Material.BOOK), MenuText.DESCRIPTION_NAME), MenuText.DESCRIPTION_DESC), p -> handleText(p, build.getDescription(), submitter, build));
        setLeftClickButton(2, setLore(customName(new ItemStack(Material.PAINTING), MenuText.RESOURCE_PACK_NAME), MenuText.RESOURCE_PACK_DESC), p -> handleText(p, build.getResourcePack(), submitter, build));
    }

    private void handleText(Player player, String string, Submitter submitter, Build build) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + string, GOLD));
        player.sendMessage(text(Messages.PREFIX + "Click here to find reopen the GUI.", GREEN).clickEvent(ClickEvent.runCommand("/ss view " + PaperSubmissionsUtil.getSubmitterName(submitter) + " " + build.getName())));
    }
}
