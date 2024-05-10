package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Messages;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.chest.PaperChestGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.Messages.PREFIX;
import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;
import static io.musician101.musigui.paper.chest.PaperIconUtil.customName;
import static io.musician101.musigui.paper.chest.PaperIconUtil.setLore;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public abstract class BuildGUI extends PaperChestGUI<ServerSaturday> {

    protected BuildGUI(@NotNull Build build, @NotNull Submitter submitter, int featureSlot, int teleportSlot, @NotNull Player player) {
        super(player, text(build.getName()), 9, getPlugin(), false);
        Location location = build.getLocation();
        List<Component> lore = teleportDesc(location);
        ItemStack itemStack = setLore(customName(new ItemStack(Material.COMPASS), text("Teleport")), lore);
        setLeftClickButton(teleportSlot, itemStack, p -> {
            if (p.hasPermission("ss.view.goto")) {
                p.teleport(location);
                p.sendMessage(text(PREFIX + "You have teleported to " + build.getName(), GREEN));
                return;
            }

            p.sendMessage(text(PREFIX + "You don't have permission to run this command.", RED));
        });

        updateFeatured(build, submitter, featureSlot);
        setLeftClickButton(8, customName(new ItemStack(Material.BARRIER), text("Back", WHITE)), Player::closeInventory);
    }

    @NotNull
    static List<Component> teleportDesc(@NotNull Location location) {
        return Stream.of("Click to teleport.", "- World: " + location.getWorld().getName(), "- X: " + location.getBlockX(), "- Y: " + location.getBlockY(), "- Z: " + location.getBlockZ()).map(Component::text).collect(Collectors.toList());
    }

    public static void open(@NotNull Build build, @NotNull Submitter submitter, @NotNull Player player) {
        if (player.getUniqueId().equals(submitter.getUUID())) {
            new EditBuildGUI(build, submitter, player);
            return;
        }

        new ViewBuildGUI(build, submitter, player);
    }

    protected void setLeftClickButton(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<Player> action) {
        setButton(slot, itemStack, Map.of(ClickType.LEFT, action));
    }

    private void updateFeatured(@NotNull Build build, @NotNull Submitter submitter, int featureSlot) {
        if (player.hasPermission("ss.feature")) {
            List<Component> lore = new ArrayList<>();
            lore.add(join(noSeparators(), text("Has been featured? ", GOLD), build.featured() ? text("Yes", GREEN) : text("No", RED)));
            lore.addAll(List.of(text("Set whether this build has been covered in"), text("an episode of Server Saturday.")));
            setLeftClickButton(featureSlot, setLore(customName(new ItemStack(Material.GOLDEN_APPLE), text("Feature")), lore), p -> {
                build.setFeatured(!build.featured());
                if (build.featured()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(submitter.getUUID());
                    getPlugin().getRewardHandler().giveReward(offlinePlayer);
                    Player player = offlinePlayer.getPlayer();
                    if (player != null) {
                        player.sendMessage(Messages.REWARDS_WAITING);
                    }
                }

                updateFeatured(build, submitter, featureSlot);
            });
        }
    }
}
