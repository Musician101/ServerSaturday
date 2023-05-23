package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.PaperTextInput;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.paper.chest.PaperIconUtil.customName;
import static io.musician101.musigui.paper.chest.PaperIconUtil.setLore;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, submitter, 7, 5, player);
        setLeftClickButton(0, setLore(customName(new ItemStack(Material.PAPER), MenuText.RENAME_NAME), MenuText.RENAME_DESC), p -> {
            p.sendMessage(Messages.SET_BUILD_NAME);
            handleTextInput(p, build.getName(), (ply, message) -> {
                if (submitter.getBuild(message).isPresent()) {
                    player.sendMessage(Messages.BUILD_ALREADY_EXISTS);
                    return;
                }

                build.setName(message);
                new EditBuildGUI(build, submitter, player);
            });
        });
        updateLocation(build);
        setLeftClickButton(2, setLore(customName(new ItemStack(Material.BOOK), MenuText.CHANGE_DESCRIPTION_NAME), MenuText.CHANGE_DESCRIPTION_DESC), p -> {
            p.sendMessage(text(Messages.PREFIX + "Enter your new description."));
            handleTextInput(player, build.getDescription(), (ply, s) -> {
                build.setDescription(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        setLeftClickButton(3, setLore(customName(new ItemStack(Material.PAINTING), MenuText.CHANGE_RESOURCE_PACKS_NAME), MenuText.CHANGE_RESOURCES_PACK_DESC), p -> {
            p.sendMessage(text(Messages.PREFIX + "Enter your new resourcepack."));
            handleTextInput(p, build.getResourcePack(), (ply, s) -> {
                build.setResourcePack(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        updateSubmitted(build);
    }

    private void handleTextInput(Player player, String original, BiConsumer<Player, String> action) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + "Click here to edit the original.").color(GREEN).clickEvent(ClickEvent.suggestCommand(original)));
        new PaperTextInput(ServerSaturday.getPlugin(), player) {

            @Override
            protected <T> void process(Player player, T message) {
                action.accept(player, PlainTextComponentSerializer.plainText().serializeOr((Component) message, ""));
            }
        };
    }

    private void updateLocation(@Nonnull Build build) {
        setLeftClickButton(1, setLore(customName(new ItemStack(Material.COMPASS), MenuText.CHANGE_LOCATION_NAME), MenuText.CHANGE_LOCATION_DESC), p -> {
            build.setLocation(p.getLocation());
            updateLocation(build);
            p.sendMessage(Messages.locationChanged(build));
        });
    }

    private void updateSubmitted(@Nonnull Build build) {
        Stream<Component> submitted = Stream.of(join(noSeparators(), text("Has been submitted? ", GOLD), (build.submitted() ? text("Yes", GREEN) : text("No", RED))));
        List<Component> submittedDescription = Stream.concat(submitted, MenuText.SUBMIT_UNREADY_DESC.stream()).toList();
        setLeftClickButton(4, setLore(customName(new ItemStack(Material.FLINT_AND_STEEL), MenuText.SUBMIT_UNREADY_NAME), submittedDescription), p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        });
    }
}
