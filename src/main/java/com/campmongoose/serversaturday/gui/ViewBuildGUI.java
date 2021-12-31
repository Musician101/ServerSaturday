package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, submitter, 3, 0, player);
        setButton(1, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleText(p, build.getDescription(), submitter, build)));
        setButton(2, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleText(p, build.getResourcePack(), submitter, build)));
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleText(Player player, String string, Submitter submitter, Build build) {
        player.closeInventory();
        player.sendMessage(ChatColor.GOLD + Messages.PREFIX + string);
        player.sendMessage(Component.text(Messages.PREFIX + "Click here to find reopen the GUI.").color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/ss view " + submitter.getName() + " " + build.getName())));
    }
}
