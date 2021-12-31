package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.SpigotTextInput;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, 7, 5, player);
        setButton(0, SpigotIconBuilder.builder(Material.PAPER).name(MenuText.RENAME_NAME).description(MenuText.RENAME_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            p.closeInventory();
            p.sendMessage(ChatColor.GREEN + Messages.SET_BUILD_NAME);
            new SpigotTextInput(SpigotServerSaturday.instance(), p) {

                @Override
                protected void process(Player player, String s) {
                    if (submitter.getBuild(s) != null) {
                        player.sendMessage(Messages.BUILD_ALREADY_EXISTS);
                        return;
                    }

                    submitter.renameBuild(s, build);
                    new EditBuildGUI(build, submitter, player);
                }
            };
        }));
        updateLocation(build);
        setButton(2, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.CHANGE_DESCRIPTION_NAME).description(MenuText.CHANGE_DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleTextInput(player, build.getDescription(), (ply, s) -> {
            build.setDescription(s);
            new EditBuildGUI(build, submitter, ply);
        })));
        setButton(3, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.CHANGE_RESOURCE_PACKS_NAME).description(MenuText.CHANGE_RESOURCES_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> handleTextInput(p, build.getResourcePack(), (ply, s) -> {
            build.setResourcePack(s);
            new EditBuildGUI(build, submitter, ply);
        })));
        updateSubmitted(build);
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleTextInput(Player player, String original, BiConsumer<Player, String> action) {
        player.closeInventory();
        TextComponent text = new TextComponent(Messages.PREFIX + "Click here to edit the original.");
        text.setColor(ChatColor.GREEN.asBungee());
        text.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, original));
        player.spigot().sendMessage(text);
        new SpigotTextInput(SpigotServerSaturday.instance(), player) {

            @Override
            protected void process(Player player, String s) {
                action.accept(player, s);
            }
        };
    }

    private void updateLocation(@Nonnull Build build) {
        setButton(1, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.CHANGE_LOCATION_NAME).description(MenuText.CHANGE_LOCATION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            build.setLocation(toLibLocation(p.getLocation()));
            updateLocation(build);
            p.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
        }));
    }

    private void updateSubmitted(@Nonnull Build build) {
        List<String> submittedDescription = Stream.concat(Stream.of(ChatColor.GOLD + "Has been submitted? " + (build.submitted() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")), MenuText.SUBMIT_UNREADY_DESC.stream()).collect(Collectors.toList());
        setButton(4, SpigotIconBuilder.builder(Material.FLINT_AND_STEEL).name(MenuText.SUBMIT_UNREADY_NAME).description(submittedDescription).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        }));
    }
}
