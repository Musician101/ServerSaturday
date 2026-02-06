package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Messages;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ViewBuildGUI extends BuildGUI {

    ViewBuildGUI(Build build, Submitter submitter, Player player) {
        super(build, submitter, 3, 0, player);
    }

    @Override
    public void update() {
        descriptionButton();
        resourcePackButton();
    }

    private void resourcePackButton() {
        ItemStack itemStack = new ItemStack(Material.PAINTING);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Resource Packs"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(text("View this build's recommended resource packs."))));
        setLeftClickButton(2, itemStack, p -> handleText(p, build.resourcePack(), submitter, build));
    }

    private void descriptionButton() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Description"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(text("View this build's description."))));
        setLeftClickButton(1, itemStack, p -> handleText(p, build.description(), submitter, build));
    }

    private void handleText(Player player, String string, Submitter submitter, Build build) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + string, GOLD));
        player.sendMessage(text(Messages.PREFIX + "Click here to find reopen the GUI.", GREEN).clickEvent(ClickEvent.runCommand("/ss view " + submitter.name() + " " + build.name())));
    }
}
