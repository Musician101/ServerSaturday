package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.spigot.gui.SpigotBookGUI;
import com.campmongoose.serversaturday.spigot.gui.SpigotIconBuilder;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ViewBuildGUI extends BuildGUI {

    public ViewBuildGUI(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player) {
        super(build, 3, 0, player);
        setButton(1, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> SpigotBookGUI.openWrittenBook(p, build, submitter)));
        setButton(2, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> SpigotBookGUI.openWrittenBook(p, build, submitter)));
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }
}
