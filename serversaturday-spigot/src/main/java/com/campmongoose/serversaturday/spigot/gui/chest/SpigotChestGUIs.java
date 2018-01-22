package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.gui.chest.ChestGUIs;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.gui.SpigotBookGUI;
import com.campmongoose.serversaturday.spigot.gui.SpigotIconBuilder;
import com.campmongoose.serversaturday.spigot.gui.anvil.SSAnvilGUI;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
    public Optional<SpigotChestGUI> allSubmissions(int page, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        Multimap<SpigotBuild, SpigotSubmitter> map = HashMultimap.create();
        SpigotServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().forEach(build -> map.put(build, submitter)));
        return Optional.of(paged(MenuText.ALL_SUBMISSIONS, player, prevGUI, page, ClickType.LEFT, new ArrayList<>(map.keySet()), build -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            return build.getMenuRepresentation(submitter);
        }, (p, build) -> {
            SpigotSubmitter submitter = new ArrayList<>(map.get(build)).get(0);
            map.remove(build, submitter);
            return (g, ply) -> {
                if (ply.getUniqueId().equals(submitter.getUUID())) {
                    editBuild(build, submitter, ply, g);
                }
                else {
                    viewBuild(build, submitter, ply, g);
                }
            };
        }, (p, i) -> allSubmissions(i, p, prevGUI)).build());
    }

    @Nonnull
    protected SpigotChestGUIBuilder build(int featureSlot, int teleportSlot, @Nonnull SpigotBuild build, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        Location location = build.getLocation();
        SpigotChestGUIBuilder builder = builder(9, 8, build.getName(), player, prevGUI).setButton(new GUIButton<>(teleportSlot, ClickType.LEFT, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.TELEPORT_NAME).description(MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).build(), (g, p) -> {
            if (player.hasPermission(Permissions.VIEW_GOTO)) {
                player.teleport(location);
                player.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                return;
            }

            player.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
        }));

        if (player.hasPermission(Permissions.FEATURE)) {
            builder.setButton(new GUIButton<>(featureSlot, ClickType.LEFT, SpigotIconBuilder.builder(Material.GOLDEN_APPLE).name(MenuText.FEATURE_NAME).description(MenuText.FEATURE_DESC).addGlow(build.featured()).build(), (g, p) -> {
                build.setFeatured(!build.featured());
                g.open();
            }));
        }

        return builder;
    }

    @Nonnull
    protected SpigotChestGUIBuilder builder(int size, int backButtonSlot, @Nonnull String name, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        return SpigotChestGUI.builder().setSize(size).setName(name).setPlayer(player).setPreviousGUI(prevGUI).setBackButton(backButtonSlot, ClickType.LEFT);
    }

    @Nonnull
    public Optional<SpigotChestGUI> editBuild(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        return Optional.of(build(7, 5, build, player, prevGUI).setButton(new GUIButton<>(0, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAPER).name(MenuText.RENAME_NAME).description(MenuText.RENAME_DESC).build(), (g, p) -> new SSAnvilGUI(player, (ply, s) -> {
            submitter.renameBuild(s, build);
            editBuild(build, submitter, ply, prevGUI);
            return null;
        }))).setButton(new GUIButton<>(1, ClickType.LEFT, SpigotIconBuilder.builder(Material.COMPASS).name(MenuText.CHANGE_LOCATION_NAME).description(MenuText.CHANGE_LOCATION_DESC).build(), (g, p) -> {
            build.setLocation(player.getLocation());
            g.open();
            player.sendMessage(ChatColor.GREEN + Messages.locationChanged(build));
        })).setButton(new GUIButton<>(2, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.CHANGE_DESCRIPTION_NAME).description(MenuText.CHANGE_DESCRIPTION_DESC).build(), (g, p) -> {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (SpigotBookGUI.isEditing(p)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            p.closeInventory();
            new SpigotBookGUI(p, build, build.getDescription(), pages -> {
                build.setDescription(pages);
                g.open();
            });
        })).setButton(new GUIButton<>(3, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.CHANGE_RESOURCE_PACKS_NAME).description(MenuText.CHANGE_RESOURCES_PACK_DESC).build(), (g, p) -> {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (SpigotBookGUI.isEditing(p)) {
                player.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            p.closeInventory();
            new SpigotBookGUI(p, build, build.getResourcePack(), pages -> {
                build.setResourcePack(pages);
                g.open();
            });
        })).setButton(new GUIButton<>(4, ClickType.LEFT, SpigotIconBuilder.builder(Material.FLINT_AND_STEEL).name(MenuText.SUBMIT_UNREADY_NAME).description(MenuText.SUBMIT_UNREADY_DESC).addGlow(build.submitted()).build(), (g, p) -> {
            build.setSubmitted(!build.submitted());
            g.open();
        })).setButton(new GUIButton<>(6, ClickType.LEFT, SpigotIconBuilder.builder(Material.ENDER_CHEST).name(MenuText.DELETE_NAME).description(MenuText.DELETE_DESC).build(), (g, p) -> {
            submitter.removeBuild(build.getName());
            if (prevGUI != null) {
                prevGUI.open();
            }
        })).build()).filter(gui -> player.getUniqueId().equals(submitter.getUUID()));
    }

    @Nonnull
    protected <O> SpigotChestGUIBuilder paged(@Nonnull String name, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI, int page, @Nonnull ClickType clickType, @Nonnull List<O> contents, @Nonnull Function<O, ItemStack> itemStackMapper, @Nullable BiFunction<Player, O, BiConsumer<SpigotChestGUI, Player>> actionMapper, @Nonnull BiConsumer<Player, Integer> pageNavigator) {
        int maxPage = new Double(Math.ceil(contents.size() / 45)).intValue();
        return builder(54, 53, name, player, prevGUI).setPage(page).setContents(clickType, contents, itemStackMapper, actionMapper)
                .setJumpToPage(45, maxPage, pageNavigator)
                .setPageNavigation(48, MenuText.PREVIOUS_PAGE, (gui, p) -> {
                    if (page > 1) {
                        pageNavigator.accept(p, page - 1);
                    }
                })
                .setPageNavigation(50, MenuText.NEXT_PAGE, (gui, p) -> {
                    if (page + 1 > maxPage) {
                        pageNavigator.accept(p, page + 1);
                    }
                });
    }

    @Nonnull
    public Optional<SpigotChestGUI> submissions(int page, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        return Optional.of(paged(MenuText.SUBMISSIONS, player, prevGUI, page, ClickType.LEFT, SpigotServerSaturday.instance().getSubmissions().getSubmitters(), SpigotSubmitter::getMenuRepresentation, (p, submitter) -> (g, ply) -> submitter(1, ply, submitter, g), (p, i) -> submissions(i, p, prevGUI)).build());
    }

    @Nonnull
    public Optional<SpigotChestGUI> submitter(int page, @Nonnull Player player, @Nonnull SpigotSubmitter submitter, @Nullable SpigotChestGUI prevGUI) {
        return Optional.of(paged(MenuText.submitterMenu(submitter), player, prevGUI, page, ClickType.LEFT, submitter.getBuilds(), build -> build.getMenuRepresentation(submitter), (p, build) -> (g, ply) -> {
            if (ply.getUniqueId().equals(submitter.getUUID())) {
                editBuild(build, submitter, player, g);
            }
            else {
                viewBuild(build, submitter, player, g);
            }
        }, (p, i) -> submitter(i, p, submitter, prevGUI)).build());
    }

    @Nonnull
    public Optional<SpigotChestGUI> viewBuild(@Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter, @Nonnull Player player, @Nullable SpigotChestGUI prevGUI) {
        return Optional.of(build(3, 0, build, player, prevGUI).setButton(new GUIButton<>(1, ClickType.LEFT, SpigotIconBuilder.builder(Material.BOOK).name(MenuText.DESCRIPTION_NAME).description(MenuText.DESCRIPTION_DESC).build(), (g, p) -> SpigotBookGUI.openWrittenBook(player, build, submitter))).setButton(new GUIButton<>(2, ClickType.LEFT, SpigotIconBuilder.builder(Material.PAINTING).name(MenuText.RESOURCE_PACK_NAME).description(MenuText.RESOURCE_PACK_DESC).build(), (g, p) -> SpigotBookGUI.openWrittenBook(player, build, submitter))).build()).filter(gui -> !player.getUniqueId().equals(submitter.getUUID()));
    }
}
