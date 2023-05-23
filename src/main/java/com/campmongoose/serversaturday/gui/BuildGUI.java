package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.paper.chest.PaperChestGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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

    protected BuildGUI(@Nonnull Build build, @Nonnull Submitter submitter, int featureSlot, int teleportSlot, @Nonnull Player player) {
        super(player, text(build.getName()), 9, getPlugin(), false);
        Location location = build.getLocation();
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

    protected void setLeftClickButton(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> action) {
        setButton(slot, itemStack, Map.of(ClickType.LEFT, action));
    }

    public static void open(@Nonnull Build build, @Nonnull Submitter submitter, @Nonnull Player player) {
        if (player.getUniqueId().equals(submitter.getUUID())) {
            new EditBuildGUI(build, submitter, player);
            return;
        }

        new ViewBuildGUI(build, submitter, player);
    }

    private void updateFeatured(@Nonnull Build build, @Nonnull Submitter submitter, int featureSlot) {
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
