package com.campmongoose.serversaturday.paper.gui;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.paper.PaperServerSaturday;
import com.campmongoose.serversaturday.paper.submission.PaperSubmissionsUtil;
import io.musician101.musigui.paper.chest.PaperChestGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.campmongoose.serversaturday.paper.PaperServerSaturday.getPlugin;
import static io.musician101.musigui.paper.chest.PaperIconUtil.customName;
import static io.musician101.musigui.paper.chest.PaperIconUtil.setLore;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public abstract class BuildGUI extends PaperChestGUI<PaperServerSaturday> {

    protected BuildGUI(@NotNull Build build, @NotNull Submitter submitter, int featureSlot, int teleportSlot, @NotNull Player player) {
        super(player, text(build.getName()), 9, getPlugin(), false);
        Location location = PaperSubmissionsUtil.asLocation(build.getLocation());
        List<Component> lore = MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ItemStack itemStack = setLore(customName(new ItemStack(Material.COMPASS), MenuText.TELEPORT_NAME), lore);
        setLeftClickButton(teleportSlot, itemStack, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.teleport(location);
                p.sendMessage(text(Messages.teleportedToBuild(build), GREEN));
                return;
            }

            p.sendMessage(Messages.NO_PERMISSION);
        });

        updateFeatured(build, submitter, featureSlot);
        setLeftClickButton(8, customName(new ItemStack(Material.BARRIER), MenuText.BACK), Player::closeInventory);
    }

    protected void setLeftClickButton(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<Player> action) {
        setButton(slot, itemStack, Map.of(ClickType.LEFT, action));
    }

    public static void open(@NotNull Build build, @NotNull Submitter submitter, @NotNull Player player) {
        if (player.getUniqueId().equals(submitter.getUUID())) {
            new EditBuildGUI(build, submitter, player);
            return;
        }

        new ViewBuildGUI(build, submitter, player);
    }

    private void updateFeatured(@NotNull Build build, @NotNull Submitter submitter, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<Component> lore = new ArrayList<>();
            lore.add(join(noSeparators(), text("Has been featured? ", GOLD), build.featured() ? text("Yes", GREEN) : text("No", RED)));
            lore.addAll(MenuText.FEATURE_DESC);
            setLeftClickButton(featureSlot, setLore(customName(new ItemStack(Material.GOLDEN_APPLE), MenuText.FEATURE_NAME.color(WHITE)), lore), p -> {
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
