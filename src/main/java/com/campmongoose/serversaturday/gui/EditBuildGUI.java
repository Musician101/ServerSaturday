package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.spigot.SpigotTextInput;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;
import static io.musician101.musigui.spigot.chest.SpigotIconUtil.setLore;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        super(build, submitter, 7, 5, player);
        setButton(0, setLore(customName(new ItemStack(Material.PAPER), MenuText.RENAME_NAME), MenuText.RENAME_DESC), ClickType.LEFT, p -> {
            p.closeInventory();
            p.sendMessage(Messages.SET_BUILD_NAME);
            new SpigotTextInput(ServerSaturday.getInstance(), p) {

                @Override
                protected void process(Player player, String s) {
                    if (submitter.getBuild(s).isPresent()) {
                        player.sendMessage(Messages.BUILD_ALREADY_EXISTS);
                        return;
                    }

                    build.setName(s);
                    new EditBuildGUI(build, submitter, player);
                }
            };
        });
        updateLocation(build);
        setButton(2, setLore(customName(new ItemStack(Material.BOOK), MenuText.CHANGE_DESCRIPTION_NAME), MenuText.CHANGE_DESCRIPTION_DESC), ClickType.LEFT, p -> handleTextInput(player, build.getDescription(), (ply, s) -> {
            build.setDescription(s);
            new EditBuildGUI(build, submitter, ply);
        }));
        setButton(3, setLore(customName(new ItemStack(Material.PAINTING), MenuText.CHANGE_RESOURCE_PACKS_NAME), MenuText.CHANGE_RESOURCES_PACK_DESC), ClickType.LEFT, p -> handleTextInput(p, build.getResourcePack(), (ply, s) -> {
            build.setResourcePack(s);
            new EditBuildGUI(build, submitter, ply);
        }));
        updateSubmitted(build);
        setButton(8, customName(new ItemStack(Material.BARRIER), "Back"), ClickType.LEFT, p -> new SubmitterGUI(submitter, p));
    }

    private void handleTextInput(Player player, String original, BiConsumer<Player, String> action) {
        player.closeInventory();
        player.sendMessage(Component.text(Messages.PREFIX + "Click here to edit the original.").color(NamedTextColor.GREEN).clickEvent(ClickEvent.suggestCommand(original)));
        new SpigotTextInput(ServerSaturday.getInstance(), player) {

            @Override
            protected void process(Player player, String s) {
                action.accept(player, s);
            }
        };
    }

    private void updateLocation(@Nonnull Build build) {
        setButton(1, setLore(customName(new ItemStack(Material.COMPASS), MenuText.CHANGE_LOCATION_NAME), MenuText.CHANGE_LOCATION_DESC), ClickType.LEFT, p -> {
            build.setLocation(p.getLocation());
            updateLocation(build);
            p.sendMessage(Messages.locationChanged(build));
        });
    }

    private void updateSubmitted(@Nonnull Build build) {
        List<String> submittedDescription = Stream.concat(Stream.of(ChatColor.GOLD + "Has been submitted? " + (build.submitted() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")), MenuText.SUBMIT_UNREADY_DESC.stream()).collect(Collectors.toList());
        setButton(4, setLore(customName(new ItemStack(Material.FLINT_AND_STEEL), MenuText.SUBMIT_UNREADY_NAME), submittedDescription), ClickType.LEFT, p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        });
    }
}
