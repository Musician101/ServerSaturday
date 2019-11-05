package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.SpigotBookGUI;
import com.campmongoose.serversaturday.spigot.gui.SpigotIconBuilder;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.campmongoose.serversaturday.spigot.textinput.SpigotTextInput;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpigotChestGUIs extends ChestGUIs<SpigotChestGUIBuilder, ClickType, SpigotChestGUI, Inventory, SpigotBuild, Location, Player, ItemStack, String, SpigotSubmitter> {

    public static final ChestGUIs<SpigotChestGUIBuilder, ClickType, SpigotChestGUI, Inventory, SpigotBuild, Location, Player, ItemStack, String, SpigotSubmitter> INSTANCE = new SpigotChestGUIs();

    private SpigotChestGUIs() {

    }

    @Nonnull
    @Override
    public Optional<SpigotChestGUI> allSubmissions(int page, @Nonnull Player player) {
        Multimap<SpigotBuild, SpigotSubmitter> map = TreeMultimap.create(Comparator.comparing(SpigotBuild::getName), Comparator.comparing(SpigotSubmitter::getName));
        SpigotServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().forEach(build -> map.put(build, submitter)));
        return Optional.of(paged(MenuText.ALL_SUBMISSIONS, player, page, ClickType.LEFT, new ArrayList<>(map.keySet()), build -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            return build.getMenuRepresentation(submitter);
        }, build -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            map.remove(build, submitter);
            return p -> {
                if (p.getUniqueId().equals(submitter.getUUID())) {
                    editBuild(build, submitter, p);
                }
                else {
                    viewBuild(build, submitter, p);
                }
            };
        }, (p, i) -> allSubmissions(i, p)).setButton(new GUIButton<>(53, ClickType.LEFT, SpigotIconBuilder.of(Material.BARRIER, "Back"), Player::closeInventory)).build());
    }

    @Nonnull
    @Override
    protected SpigotChestGUIBuilder build(int featureSlot, int teleportSlot, @Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player) {
        Location location = build.getLocation();
        SpigotChestGUIBuilder builder = builder(9, build.getName(), player).setButton(new GUIButton<>(teleportSlot, ClickType.LEFT, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.TELEPORT_NAME).description(MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).build(), p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                player.teleport(location);
                player.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                return;
            }

            player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
        }));

        if (player.hasPermission(Permissions.FEATURE)) {
            builder.setButton(new GUIButton<>(featureSlot, ClickType.LEFT, SpigotIconBuilder.builder(Material.GOLDEN_APPLE).name(MenuText.FEATURE_NAME).description(MenuText.FEATURE_DESC).addGlow(build.featured()).build(), p ->{
                build.setFeatured(!build.featured());
                if (featureSlot == 7) {
                    editBuild(build, submitter, p);
                    return;
                }

                viewBuild(build, submitter, p);
            }));
        }

        return builder;
    }

    @Nonnull
    @Override
    protected SpigotChestGUIBuilder builder(int size, @Nonnull String name, @Nonnull Player player) {
        return SpigotChestGUI.builder().setSize(size).setName(name).setPlayer(player);
    }

    @Nonnull
    @Override
    public Optional<SpigotChestGUI> editBuild(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player) {
        return Optional.of(build(7, 5, build, submitter, player).setButton(new GUIButton<>(0, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAPER).name(MenuText.RENAME_NAME).description(MenuText.RENAME_DESC).build(), p -> {
            p.closeInventory();
            p.sendMessage(ChatColor.GREEN + Messages.SET_BUILD_NAME);
            SpigotTextInput.addPlayer(p, (ply, s) -> {
                submitter.renameBuild(s, build);
                editBuild(build, submitter, ply);
            });
        })).setButton(new GUIButton<>(1, ClickType.LEFT, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.CHANGE_LOCATION_NAME).description(MenuText.CHANGE_LOCATION_DESC).build(), p ->{
            build.setLocation(p.getLocation());
            editBuild(build, submitter, p);
            player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
        })).setButton(new GUIButton<>(2, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.CHANGE_DESCRIPTION_NAME).description(MenuText.CHANGE_DESCRIPTION_DESC).build(), p ->{
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (SpigotBookGUI.isEditing(p)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            player.closeInventory();
            new SpigotBookGUI(p, build, build.getDescription(), pages -> {
                build.setDescription(pages);
                editBuild(build, submitter, p);
            });
        })).setButton(new GUIButton<>(3, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.CHANGE_RESOURCE_PACKS_NAME).description(MenuText.CHANGE_RESOURCES_PACK_DESC).build(), p ->{
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (SpigotBookGUI.isEditing(p)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            player.closeInventory();
            new SpigotBookGUI(p, build, build.getResourcePacks(), pages -> {
                build.setResourcePacks(pages);
                editBuild(build, submitter, p);
            });
        })).setButton(new GUIButton<>(4, ClickType.LEFT, SpigotIconBuilder.builder(Material.FLINT_AND_STEEL).name(MenuText.SUBMIT_UNREADY_NAME).description(MenuText.SUBMIT_UNREADY_DESC).addGlow(build.submitted()).build(), p ->{
            build.setSubmitted(!build.submitted());
            editBuild(build, submitter, p);
        })).setButton(new GUIButton<>(6, ClickType.LEFT, SpigotIconBuilder.builder(Material.ENDER_CHEST).name(MenuText.DELETE_NAME).description(MenuText.DELETE_DESC).build(), p ->{
            submitter.removeBuild(build.getName());
            submitter(1, p, submitter);
        })).setButton(new GUIButton<>(8, ClickType.LEFT, SpigotIconBuilder.of(Material.BARRIER, "Back"), p ->submitter(1, p, submitter))).build()).filter(gui -> player.getUniqueId().equals(submitter.getUUID()));
    }

    @Nonnull
    @Override
    protected <O> SpigotChestGUIBuilder paged(@Nonnull String name, @Nonnull Player player, int page, @Nonnull ClickType clickType, @Nonnull List<O> contents, @Nonnull Function<O, ItemStack> itemStackMapper, @Nullable Function<O, Consumer<Player>> actionMapper, @Nonnull BiConsumer<Player, Integer> pageNavigator) {
        int maxPage = new Double(Math.ceil(contents.size() / 45D)).intValue();
        return builder(54, name, player).setPage(page).setContents(clickType, contents, itemStackMapper, actionMapper)
                .setJumpToPage(45, maxPage, pageNavigator)
                .setPageNavigation(48, MenuText.PREVIOUS_PAGE, p ->{
                    if (page > 1) {
                        pageNavigator.accept(p, page - 1);
                    }
                })
                .setPageNavigation(50, MenuText.NEXT_PAGE, p ->{
                    if (page + 1 <= maxPage) {
                        pageNavigator.accept(p, page + 1);
                    }
                });
    }

    @Nonnull
    @Override
    public Optional<SpigotChestGUI> submissions(int page, @Nonnull Player player) {
        List<SpigotSubmitter> submitters = SpigotServerSaturday.instance().getSubmissions().getSubmitters();
        submitters.sort(Comparator.comparing(SpigotSubmitter::getName));
        return Optional.of(paged(MenuText.SUBMISSIONS, player, page, ClickType.LEFT, submitters, SpigotSubmitter::getMenuRepresentation, submitter -> p -> submitter(1, p, submitter), (p, i) -> submissions(i, p)).setButton(new GUIButton<>(53, ClickType.LEFT, SpigotIconBuilder.of(Material.BARRIER, "Back"), Player::closeInventory)).build());
    }

    @Nonnull
    @Override
    public Optional<SpigotChestGUI> submitter(int page, @Nonnull Player player, @Nonnull SpigotSubmitter submitter) {
        List<SpigotBuild> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(SpigotBuild::getName));
        return Optional.of(paged(MenuText.submitterMenu(submitter), player, page, ClickType.LEFT, builds, build -> build.getMenuRepresentation(submitter), build -> p -> {
            if (p.getUniqueId().equals(submitter.getUUID())) {
                editBuild(build, submitter, p);
            }
            else {
                viewBuild(build, submitter, p);
            }
        }, (p, i) -> submitter(i, p, submitter)).setButton(new GUIButton<>(53, ClickType.LEFT, SpigotIconBuilder.of(Material.BARRIER, "Back"), p ->submissions(1, p))).build());
    }

    @Nonnull
    @Override
    public Optional<SpigotChestGUI> viewBuild(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player) {
        return Optional.of(build(3, 0, build, submitter, player).setButton(new GUIButton<>(1, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), p ->SpigotBookGUI.openWrittenBook(p, build, submitter))).setButton(new GUIButton<>(2, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), p -> SpigotBookGUI.openWrittenBook(p, build, submitter))).setButton(new GUIButton<>(8, ClickType.LEFT, SpigotIconBuilder.of(Material.BARRIER, "Back"), p ->submitter(1, p, submitter))).build()).filter(gui -> !player.getUniqueId().equals(submitter.getUUID()));
    }
}
