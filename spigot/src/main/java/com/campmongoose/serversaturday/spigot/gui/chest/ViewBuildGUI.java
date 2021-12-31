package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, 3, 0, player);
        setButton(1, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            handleText(p, build.getDescription());
            p.sendMessage(ChatColor.GREEN + Messages.PREFIX + build.getDescription());
        }));
        setButton(2, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> p.sendMessage(ChatColor.GREEN + Messages.PREFIX + build.getResourcePack())));
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleText(Player player, String string) {
        player.sendMessage(ChatColor.GREEN + Messages.PREFIX + string);
        TextComponent text = new TextComponent(Messages.PREFIX + "Click here to find reopen the GUI.");
        text.setColor(ChatColor.GREEN.asBungee());
        text.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/ssview"));
        player.spigot().sendMessage(text);
    }
}
