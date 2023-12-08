package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissionsUtil;
import io.musician101.musigui.sponge.chest.SpongeChestGUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.world.server.ServerLocation;

import static com.campmongoose.serversaturday.sponge.SpongeServerSaturday.getPlugin;
import static io.musician101.musigui.sponge.chest.SpongeIconUtil.customName;
import static io.musician101.musigui.sponge.chest.SpongeIconUtil.setLore;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public abstract class BuildGUI extends SpongeChestGUI {

    protected BuildGUI(@NotNull Build build, @NotNull Submitter submitter, int featureSlot, int teleportSlot, @NotNull ServerPlayer player) {
        super(player, text(build.getName()), 9, getPlugin().getPluginContainer(), false, false);
        ServerLocation location = SpongeSubmissionsUtil.asLocation(build.getLocation());
        List<Component> lore = MenuText.teleportDesc(location.world().properties().name(), location.blockX(), location.blockY(), location.blockZ());
        ItemStack itemStack = setLore(customName(ItemStack.of(ItemTypes.COMPASS), MenuText.TELEPORT_NAME), lore);
        setLeftClickButton(teleportSlot, itemStack, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.setLocation(location);
                p.setRotation(SpongeSubmissionsUtil.asRotation(build.getLocation()));
                p.sendMessage(text(Messages.teleportedToBuild(build), GREEN));
                return;
            }

            p.sendMessage(Messages.NO_PERMISSION);
        });

        updateFeatured(build, submitter, featureSlot);
        setLeftClickButton(8, customName(ItemStack.of(ItemTypes.BARRIER), MenuText.BACK), ServerPlayer::closeInventory);
    }

    protected void setLeftClickButton(int slot, @NotNull ItemStack itemStack, @NotNull Consumer<ServerPlayer> action) {
        setButton(slot, itemStack, Map.of(ClickTypes.CLICK_LEFT, action));
    }

    public static void open(@NotNull Build build, @NotNull Submitter submitter, @NotNull ServerPlayer player) {
        if (player.uniqueId().equals(submitter.getUUID())) {
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
            setLeftClickButton(featureSlot, setLore(customName(ItemStack.of(ItemTypes.GOLDEN_APPLE), MenuText.FEATURE_NAME.color(WHITE)), lore), p -> {
                build.setFeatured(!build.featured());
                if (build.featured()) {
                    getPlugin().getRewardHandler().giveReward(submitter.getUUID());
                    Sponge.server().player(submitter.getUUID()).ifPresent(player -> player.sendMessage(Messages.REWARDS_WAITING));
                }

                updateFeatured(build, submitter, featureSlot);
            });
        }
    }
}
