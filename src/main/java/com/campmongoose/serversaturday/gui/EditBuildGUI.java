package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.dialog.build.EditBuildTextDialog;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
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
public class EditBuildGUI extends BuildGUI {

    EditBuildGUI(Build build, Submitter submitter, Player player) {
        super(build, submitter, 7, 5, player);
        update();
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
        setCustomName(itemStack, Component.translatable("ss.gui.build.edit.rename.label"));
        setLore(itemStack, List.of(Component.translatable("ss.gui.build.edit.rename.description")));
        setLeftClickButton(0, itemStack, p -> p.showDialog(EditBuildTextDialog.rename(build)));
    }

    private void descriptionButton() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        setCustomName(itemStack, Component.translatable("ss.gui.build.edit.change-description.label"));
        setLore(itemStack, List.of(Component.translatable("ss.gui.build.edit.change-description.description")));
        setLeftClickButton(2, itemStack, p -> p.showDialog(EditBuildTextDialog.changeDescription(build)));
    }

    private void resourcePackButton() {
        ItemStack itemStack = new ItemStack(Material.PAINTING);
        setCustomName(itemStack, Component.translatable("ss.gui.build.edit.change-resource-pack.label"));
        setLore(itemStack, List.of(Component.translatable("ss.gui.build.edit.change-resource-pack.description")));
        setLeftClickButton(3, itemStack, p -> p.showDialog(EditBuildTextDialog.changeResourcePack(build)));
    }

    private void updateLocation() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        setCustomName(itemStack, Component.translatable("ss.gui.build.edit.change-location.label"));
        List<TranslatableComponent> lore = Stream.of("ss.gui.build.edit.change-location.description.main", "ss.gui.build.edit.change-location.description.warning").map(Component::translatable).collect(Collectors.toList());
        setLore(itemStack, lore);
        setLeftClickButton(1, itemStack, p -> {
            build.location(p.getLocation());
            updateLocation();
            p.sendMessage(Component.translatable("ss.gui.build.edit.change-resource-pack.description.success", Argument.tagResolver(Placeholder.unparsed("build", build.name()))));
        });
    }

    private void updateSubmitted() {
        ItemStack itemStack = new ItemStack(Material.FLINT_AND_STEEL);
        setCustomName(itemStack, Component.translatable("ss.gui.build.edit.submit.label"));
        List<TranslatableComponent> lore = new ArrayList<>();
        lore.add(Component.translatable("ss.gui.build.edit.submit.description.submitted", Argument.tagResolver(Formatter.booleanChoice("submitted", build.submitted()))));
        lore.add(Component.translatable("ss.gui.build.edit.submit.description.main"));
        setLore(itemStack, lore);
        setLeftClickButton(4, itemStack, p -> {
            build.submitted(!build.submitted());
            updateSubmitted();
        });
    }
}
