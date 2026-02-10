package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.dialog.build.EditBuildTextDialog;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class EditBuildGUI extends BuildGUI {

    EditBuildGUI(Build build, Submitter submitter, Player player) {
        super(build, submitter, 7, 5, player);
    }

    @Override
    public void update() {
        super.update();
        renameButton();
        updateLocation();
        descriptionButton();
        resourcePackButton();
        updateSubmitted();
    }

    private void renameButton() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.edit.rename.label"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.translatable("ss.gui.build.edit.rename.description"))));
        setLeftClickButton(0, itemStack, p -> p.showDialog(EditBuildTextDialog.rename(build)));
    }

    private void descriptionButton() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.edit.change-description.label"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.translatable("ss.gui.build.edit.change-description.description"))));
        setLeftClickButton(2, itemStack, p -> p.showDialog(EditBuildTextDialog.changeDescription(build)));
    }

    private void resourcePackButton() {
        ItemStack itemStack = new ItemStack(Material.PAINTING);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.edit.change-resource-pack.label"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.translatable("ss.gui.build.edit.change-resource-pack.description"))));
        setLeftClickButton(3, itemStack, p -> p.showDialog(EditBuildTextDialog.changeResourcePack(build)));
    }

    private void updateLocation() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.edit.change-location.label"));
        List<Component> lore = Stream.of("ss.gui.build.edit.change-resource-pack.description.main", "ss.gui.build.edit.change-resource-pack.description.warning").map(Component::translatable).collect(Collectors.toList());
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        setLeftClickButton(1, itemStack, p -> {
            build.location(p.getLocation());
            updateLocation();
            p.sendMessage(Component.translatable("ss.gui.build.edit.change-resource-pack.description.success", Argument.tagResolver(Placeholder.unparsed("build", build.name()))));
        });
    }

    private void updateSubmitted() {
        ItemStack itemStack = new ItemStack(Material.FLINT_AND_STEEL);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.edit.submit.label"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.translatable("ss.gui.build.edit.submit.description.submitted", Argument.tagResolver(Formatter.booleanChoice("submitted", build.submitted()))));
        lore.add(Component.translatable("ss.gui.build.edit.submit.description.main"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        setLeftClickButton(4, itemStack, p -> {
            build.submitted(!build.submitted());
            updateSubmitted();
        });
    }
}
