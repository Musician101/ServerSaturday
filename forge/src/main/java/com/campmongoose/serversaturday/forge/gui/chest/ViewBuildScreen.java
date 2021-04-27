package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.forge.gui.ForgeBookGUI;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.network.message.ViewBuildMessage;
import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.item.Items;
import org.bukkit.event.inventory.ClickType;
//TODO replace with Forge GUIs
@Deprecated
public class ViewBuildScreen extends BuildScreen {

    private final UUID viewer;

    public ViewBuildScreen(@Nonnull ViewBuildMessage message) {
        super(message.build, 3, 0);
        this.viewer = message.viewer;
        setButton(1, ForgeIconBuilder.builder(Items.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> ForgeBookGUI.openWrittenBook(p, build, submitter)));
        setButton(2, ForgeIconBuilder.builder(Items.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> ForgeBookGUI.openWrittenBook(p, build, submitter)));
        setButton(8, ForgeIconBuilder.of(Items.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterScreen(submitter, p)));
    }
}
