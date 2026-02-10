package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.chest.PaperChestGUI;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public abstract class BuildGUI extends PaperChestGUI<ServerSaturday> {

    protected final Submitter submitter;
    protected final Build build;
    private final int featureSlot;
    private final int teleportSlot;

    protected BuildGUI(Build build, Submitter submitter, int featureSlot, int teleportSlot, Player player) {
        super(player, Component.text(build.name()), 9, getPlugin(), false);
        this.build = build;
        this.submitter = submitter;
        this.featureSlot = featureSlot;
        this.teleportSlot = teleportSlot;
    }

    public static void open(Build build, Submitter submitter, Player player) {
        if (player.getUniqueId().equals(submitter.uniqueId())) {
            new EditBuildGUI(build, submitter, player);
            return;
        }

        new ViewBuildGUI(build, submitter, player);
    }

    @Override
    public void update() {
        Location location = build.location();
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.teleport.label"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(teleportDesc(location)));
        setLeftClickButton(teleportSlot, itemStack, p -> {
            if (p.hasPermission("ss.view.goto")) {
                p.teleport(location);
                ComponentLike argument = Argument.tagResolver(Placeholder.unparsed("build", build.name()));
                p.sendMessage(Component.translatable("ss.gui.build.teleport.success", argument));
                return;
            }

            p.sendMessage(Component.translatable("ss.gui.build.teleport.no-permission"));
        });

        updateFeatured(featureSlot);
        ItemStack backItem = new ItemStack(Material.BARRIER);
        backItem.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.back"));
        setLeftClickButton(8, backItem, Player::closeInventory);
    }

    private List<Component> teleportDesc(Location location) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("ss.gui.build.teleport.description.instruction"));
        list.add(Component.translatable("ss.gui.build.teleport.description.world", Argument.tagResolver(Placeholder.unparsed("world", location.getWorld().getName()))));
        list.add(Component.translatable("ss.gui.build.teleport.description.x", Argument.tagResolver(Formatter.number("x", location.getBlockX()))));
        list.add(Component.translatable("ss.gui.build.teleport.description.y", Argument.tagResolver(Formatter.number("y", location.getBlockY()))));
        list.add(Component.translatable("ss.gui.build.teleport.description.z", Argument.tagResolver(Formatter.number("z", location.getBlockZ()))));
        return list;
    }

    protected void setLeftClickButton(int slot, ItemStack itemStack, Consumer<Player> action) {
        setButton(slot, itemStack, ClickType.LEFT, action);
    }

    private void updateFeatured(int featureSlot) {
        if (player.hasPermission("ss.feature")) {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.translatable("ss.gui.build.feature.description.featured", Argument.tagResolver(Formatter.booleanChoice("featured", build.featured()))));
            lore.add(Component.translatable("ss.gui.build.feature.description.main"));
            ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
            itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.translatable("ss.gui.build.feature.label"));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore().addLines(lore).build());
            setLeftClickButton(featureSlot, itemStack, p -> {
                build.featured(!build.featured());
                if (build.featured()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(submitter.uniqueId());
                    getPlugin().getRewardHandler().giveReward(offlinePlayer);
                }

                updateFeatured(featureSlot);
            });
        }
    }
}
