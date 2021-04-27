package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.textinput.SpigotTextInput;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.common.gui.book.BookGUI.BookGUIAlreadyOpenException;
import io.musician101.musicianlibrary.java.minecraft.common.gui.book.BookGUI.PlayerHandNotEmptyException;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.book.SpigotBookGUI;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class EditBuildGUI extends BuildGUI {

    public EditBuildGUI(@Nonnull Build<String> build, @Nonnull Submitter<String> submitter, @Nonnull Player player) {
        super(build, 7, 5, player);
        setButton(0, SpigotIconBuilder.builder(Material.PAPER).name(MenuText.RENAME_NAME).description(MenuText.RENAME_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            p.closeInventory();
            p.sendMessage(ChatColor.GREEN + Messages.SET_BUILD_NAME);
            SpigotTextInput.addPlayer(p, (ply, s) -> {
                submitter.renameBuild(s, build);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        updateLocation(build);
        setButton(2, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.CHANGE_DESCRIPTION_NAME).description(MenuText.CHANGE_DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            handleBookGUI(p, build, build.getDescription(), (ply, pages) -> {
                build.setDescription(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        setButton(3, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.CHANGE_RESOURCE_PACKS_NAME).description(MenuText.CHANGE_RESOURCES_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            handleBookGUI(p, build, build.getResourcePacks(), (ply, pages) -> {
                build.setResourcePacks(pages);
                new EditBuildGUI(build, submitter, ply);
            });
        }));
        updateSubmitted(build);
        setButton(8, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
    }

    private void handleBookGUI(Player player, Build<String> build, List<String> pages, BiConsumer<Player, List<String>> action) {
        player.closeInventory();
        try {
            ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.setAuthor(player.getName());
            meta.setTitle(build.getName());
            meta.setPages(pages);
            book.setItemMeta(meta);
            new SpigotBookGUI(SpigotServerSaturday.instance(), player, book, action);
        }
        catch (PlayerHandNotEmptyException | BookGUIAlreadyOpenException e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
        }
    }

    private void updateLocation(@Nonnull Build<String> build) {
        setButton(1, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.CHANGE_LOCATION_NAME).description(MenuText.CHANGE_LOCATION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            build.setLocation(toLibLocation(p.getLocation()));
            updateLocation(build);
            p.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
        }));
    }

    private void updateSubmitted(@Nonnull Build<String> build) {
        List<String> submittedDescription = Stream.concat(Stream.of(ChatColor.GOLD + "Has been submitted? " + (build.submitted() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")), MenuText.SUBMIT_UNREADY_DESC.stream()).collect(Collectors.toList());
        setButton(4, SpigotIconBuilder.builder(Material.FLINT_AND_STEEL).name(MenuText.SUBMIT_UNREADY_NAME).description(submittedDescription).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            build.setSubmitted(!build.submitted());
            updateSubmitted(build);
        }));
    }
}
