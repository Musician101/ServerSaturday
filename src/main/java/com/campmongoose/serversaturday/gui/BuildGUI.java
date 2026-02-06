package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.chest.PaperChestGUI;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public abstract class BuildGUI extends PaperChestGUI<ServerSaturday> {

    protected final Submitter submitter;
    protected final Build build;
    private final int featureSlot;
    private final int teleportSlot;

    protected BuildGUI(Build build, Submitter submitter, int featureSlot, int teleportSlot, Player player) {
        super(player, text(build.name()), 9, getPlugin(), false);
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
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Teleport"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(teleportDesc(location)));
        setLeftClickButton(teleportSlot, itemStack, p -> {
            if (p.hasPermission("ss.view.goto")) {
                p.teleport(location);
                p.sendMessage(text(PREFIX + "You have teleported to " + build.name(), GREEN));
                return;
            }

            p.sendMessage(text(PREFIX + "You don't have permission to run this command.", RED));
        });

        updateFeatured(featureSlot);
        ItemStack backItem = new ItemStack(Material.BARRIER);
        backItem.setData(DataComponentTypes.CUSTOM_NAME, text("Back"));
        setLeftClickButton(8, backItem, Player::closeInventory);
    }

    private List<Component> teleportDesc(Location location) {
        return Stream.of("Click to teleport.", "- World: " + location.getWorld().getName(), "- X: " + location.getBlockX(), "- Y: " + location.getBlockY(), "- Z: " + location.getBlockZ()).map(Component::text).collect(Collectors.toList());
    }

    protected void setLeftClickButton(int slot, ItemStack itemStack, Consumer<Player> action) {
        setButton(slot, itemStack, ClickType.LEFT, action);
    }

    private void updateFeatured(int featureSlot) {
        if (player.hasPermission("ss.feature")) {
            List<Component> lore = new ArrayList<>();
            lore.add(join(noSeparators(), text("Has been featured? ", GOLD), build.featured() ? text("Yes", GREEN) : text("No", RED)));
            lore.addAll(List.of(text("Set whether this build has been covered in"), text("an episode of Server Saturday.")));
            ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
            itemStack.setData(DataComponentTypes.CUSTOM_NAME, text("Feature"));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore().addLines(lore).build());
            setLeftClickButton(featureSlot, itemStack, p -> {
                build.featured(!build.featured());
                if (build.featured()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(submitter.uniqueId());
                    getPlugin().getRewardHandler().giveReward(offlinePlayer);
                    Player player = offlinePlayer.getPlayer();
                    if (player != null) {
                        player.sendMessage(Messages.REWARDS_WAITING);
                    }
                }

                updateFeatured(featureSlot);
            });
        }
    }
}
