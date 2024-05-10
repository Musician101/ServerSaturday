package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.PaperTextInput;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static io.musician101.musigui.paper.chest.PaperIconUtil.customName;
import static io.musician101.musigui.paper.chest.PaperIconUtil.setLore;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@NotNull Build build, @NotNull Submitter submitter, @NotNull Player player) {
        super(build, submitter, 7, 5, player);
        setLeftClickButton(0, setLore(customName(new ItemStack(Material.PAPER), text("Rename")), text("Rename this build.")), p -> {
            p.sendMessage(text(PREFIX + "Set the name of your build.", GREEN));
            handleTextInput(p, build.getName(), (ply, message) -> {
                if (submitter.getBuild(message).isPresent()) {
                    player.sendMessage(text(PREFIX + "A build with that name already exists.", RED));
                    return;
                }

                build.setName(message);
                new EditBuildGUI(build, submitter, player);
            });
        });
        updateLocation(build);
        setLeftClickButton(2, setLore(customName(new ItemStack(Material.BOOK), text("Change Description")), text("Add or change the description to this build.")), p -> {
            p.sendMessage(text(PREFIX + "Enter your new description."));
            handleTextInput(player, build.getDescription(), (ply, s) -> {
                build.setDescription(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        setLeftClickButton(3, setLore(customName(new ItemStack(Material.PAINTING), text("Change Resource Packs")), List.of(text("Change the recommended resource"), text("packs for this build."))), p -> {
            p.sendMessage(text(PREFIX + "Enter your new resourcepack."));
            handleTextInput(p, build.getResourcePack(), (ply, s) -> {
                build.setResourcePack(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        updateSubmitted(build);
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

    private void updateLocation(@NotNull Build build) {
        List<Component> lore = Stream.of("Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build.").map(Component::text).collect(Collectors.toList());
        setLeftClickButton(1, setLore(customName(new ItemStack(Material.COMPASS), text("Change Location")), lore), p -> {
            build.setLocation(p.getLocation());
            updateLocation(build);
            p.sendMessage(text(PREFIX + "Warp location for " + build.getName() + " updated.", GREEN));
        });
    }

    private void updateSubmitted(@NotNull Build build) {
        Stream<Component> submitted = Stream.of(textOfChildren(text("Has been submitted? ", GOLD), (build.submitted() ? text("Yes", GREEN) : text("No", RED))));
        List<Component> submittedDescription = Stream.concat(submitted, Stream.of("Add or remove your build from", "the list of ready builds.").map(Component::text)).toList();
        setLeftClickButton(4, setLore(customName(new ItemStack(Material.FLINT_AND_STEEL), text("Submit/Unready")), submittedDescription), p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        });
    }
}
