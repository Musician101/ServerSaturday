package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import io.musician101.musigui.sponge.SpongeTextInput;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import static io.musician101.musigui.sponge.chest.SpongeIconUtil.customName;
import static io.musician101.musigui.sponge.chest.SpongeIconUtil.setLore;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@NotNull Build build, @NotNull Submitter submitter, @NotNull ServerPlayer player) {
        super(build, submitter, 7, 5, player);
        setLeftClickButton(0, setLore(customName(ItemStack.of(ItemTypes.PAPER), MenuText.RENAME_NAME), MenuText.RENAME_DESC),p -> {
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
        setLeftClickButton(2, setLore(customName(ItemStack.of(ItemTypes.BOOK), MenuText.CHANGE_DESCRIPTION_NAME), MenuText.CHANGE_DESCRIPTION_DESC), p -> {
            p.sendMessage(text(Messages.PREFIX + "Enter your new description."));
            handleTextInput(player, build.getDescription(), (ply, s) -> {
                build.setDescription(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        setLeftClickButton(3, setLore(customName(ItemStack.of(ItemTypes.PAINTING), MenuText.CHANGE_RESOURCE_PACKS_NAME), MenuText.CHANGE_RESOURCES_PACK_DESC), p -> {
            p.sendMessage(text(Messages.PREFIX + "Enter your new resourcepack."));
            handleTextInput(p, build.getResourcePack(), (ply, s) -> {
                build.setResourcePack(s);
                new EditBuildGUI(build, submitter, ply);
            });
        });
        updateSubmitted(build);
    }

    private void handleTextInput(ServerPlayer player, String original, BiConsumer<ServerPlayer, String> action) {
        player.closeInventory();
        player.sendMessage(text(Messages.PREFIX + "Click here to edit the original.").color(GREEN).clickEvent(ClickEvent.suggestCommand(original)));
        new SpongeTextInput(SpongeServerSaturday.getPlugin().getPluginContainer(), player) {

            @Override
            protected <T> void process(ServerPlayer player, T message) {
                action.accept(player, PlainTextComponentSerializer.plainText().serialize((Component) message));
            }
        };
    }

    private void updateLocation(@NotNull Build build) {
        setLeftClickButton(1, setLore(customName(ItemStack.of(ItemTypes.COMPASS), MenuText.CHANGE_LOCATION_NAME), MenuText.CHANGE_LOCATION_DESC), p -> {
            build.setLocation(SpongeSubmissionsUtil.asSSLocation(p.serverLocation(), p.rotation()));
            updateLocation(build);
            p.sendMessage(Messages.locationChanged(build));
        });
    }

    private void updateSubmitted(@NotNull Build build) {
        Stream<Component> submitted = Stream.of(join(noSeparators(), text("Has been submitted? ", GOLD), (build.submitted() ? text("Yes", GREEN) : text("No", RED))));
        List<Component> submittedDescription = Stream.concat(submitted, MenuText.SUBMIT_UNREADY_DESC.stream()).toList();
        setLeftClickButton(4, setLore(customName(ItemStack.of(ItemTypes.FLINT_AND_STEEL), MenuText.SUBMIT_UNREADY_NAME), submittedDescription), p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        });
    }
}
