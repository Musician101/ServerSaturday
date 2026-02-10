package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.dialog.build.ViewBuildTextDialog;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

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
        Component label = Component.translatable("ss.gui.build.view.resource-pack.label");
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, label);
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.translatable("ss.gui.build.view.resource-pack.description"))));
        setLeftClickButton(2, itemStack, p -> p.showDialog(new ViewBuildTextDialog(label, build.resourcePack(), this).build()));
    }

    private void descriptionButton() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        Component label = Component.translatable("ss.gui.build.view.description.label");
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, label);
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.translatable("ss.gui.build.view.description.description"))));
        setLeftClickButton(1, itemStack, p -> p.showDialog(new ViewBuildTextDialog(label, build.resourcePack(), this).build()));
    }
}
