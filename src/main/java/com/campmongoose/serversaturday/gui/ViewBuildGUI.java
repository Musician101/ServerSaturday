package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.Map;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;
import static io.musician101.musigui.spigot.chest.SpigotIconUtil.setLore;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, submitter, 3, 0, player);
        setButton(1, setLore(customName(new ItemStack(Material.BOOK), MenuText.DESCRIPTION_NAME), MenuText.DESCRIPTION_DESC), ClickType.LEFT, p -> handleText(p, build.getDescription(), submitter, build));
        setButton(2, setLore(customName(new ItemStack(Material.PAINTING), MenuText.RESOURCE_PACK_NAME), MenuText.RESOURCE_PACK_DESC), ClickType.LEFT, p -> handleText(p, build.getResourcePack(), submitter, build));
        setButton(8, customName(new ItemStack(Material.BARRIER), "Back"), Map.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleText(Player player, String string, Submitter submitter, Build build) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + string, GOLD));
        player.sendMessage(text(Messages.PREFIX + "Click here to find reopen the GUI.", GREEN).clickEvent(ClickEvent.runCommand("/ss view " + submitter.getName() + " " + build.getName())));
    }
}
