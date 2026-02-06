package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.PaperTextInput;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

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
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Rename"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(text("Rename this build."))));
        setLeftClickButton(0, itemStack, p -> {
            p.sendMessage(text(PREFIX + "Set the name of your build.", GREEN));
            handleTextInput(p, build.name(), (ply, message) -> {
                if (submitter.getBuild(message).isPresent()) {
                    player.sendMessage(text(PREFIX + "A build with that name already exists.", RED));
                    return;
                }

                build.name(message);
                new EditBuildGUI(build, submitter, player);
            });
        });
    }

    private void descriptionButton() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Change Description"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(text("Add or change the description to this build."))));
        setLeftClickButton(2, itemStack, p -> {
            p.sendMessage(text(PREFIX + "Enter your new description."));
            handleTextInput(player, build.description(), (ply, s) -> {
                build.description(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
    }

    private void resourcePackButton() {
        ItemStack itemStack = new ItemStack(Material.PAINTING);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Change Resource Packs"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(text("Change the recommended resource"), text("packs for this build."))));
        setLeftClickButton(3, itemStack, p -> {
            p.sendMessage(text(PREFIX + "Enter your new resource pack."));
            handleTextInput(p, build.resourcePack(), (ply, s) -> {
                build.resourcePack(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
    }

    private void handleTextInput(Player player, String original, BiConsumer<Player, String> action) {
        player.closeInventory();
        player.sendMessage(text(PREFIX + "Click here to edit the original.").color(GREEN).clickEvent(ClickEvent.suggestCommand(original)));
        new PaperTextInput(ServerSaturday.getPlugin(), player) {

            @Override
            protected <T> void process(Player player, T message) {
                action.accept(player, PlainTextComponentSerializer.plainText().serializeOr((Component) message, ""));
            }
        };
    }

    private void updateLocation() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Change Location"));
        List<Component> lore = Stream.of("Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build.").map(Component::text).collect(Collectors.toList());
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        setLeftClickButton(1, itemStack, p -> {
            build.location(p.getLocation());
            updateLocation();
            p.sendMessage(text(PREFIX + "Warp location for " + build.name() + " updated.", GREEN));
        });
    }

    private void updateSubmitted() {
        ItemStack itemStack = new ItemStack(Material.FLINT_AND_STEEL);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Submit/Unready"));
        Stream<Component> submitted = Stream.of(textOfChildren(text("Has been submitted? ", GOLD), build.submitted() ? text("Yes", GREEN) : text("No", RED)));
        List<Component> submittedDescription = Stream.concat(submitted, Stream.of("Add or remove your build from", "the list of ready builds.").map(Component::text)).toList();
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(submittedDescription));
        setLeftClickButton(4, itemStack, p -> {
            build.submitted(!build.submitted());
            updateSubmitted();
        });
    }
}
