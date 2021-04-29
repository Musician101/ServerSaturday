package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.Location;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.item.Items;
import org.bukkit.event.inventory.ClickType;

//TODO replace with Forge GUIs
@Deprecated
public abstract class BuildScreen extends SSForgeScreen {

    @Nonnull
    protected final ForgeBuild build;

    protected BuildScreen(@Nonnull ForgeBuild build, int featureSlot, int teleportSlot) {
        super(build.getName());
        this.build = build;
        Location location = build.getLocation();
        setButton(teleportSlot, ForgeIconBuilder.builder(Items.COMPASS).name(MenuText.TELEPORT_NAME).description(MenuText.teleportDesc(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ())).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            if (p.hasPermission(Permissions.VIEW_GOTO)) {
                p.teleport(location);
                p.sendMessage(ChatColor.GREEN + Messages.teleportedToBuild(build));
                return;
            }

            p.sendMessage(ChatColor.RED + Messages.NO_PERMISSION);
        }));

        updateFeatured(build, featureSlot);
    }

    private void updateFeatured(@Nonnull ForgeBuild build, int featureSlot) {
        if (player.hasPermission(Permissions.FEATURE)) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Has been featured? " + (build.featured() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
            lore.addAll(MenuText.FEATURE_DESC);
            setButton(featureSlot, ForgeIconBuilder.builder(Items.GOLDEN_APPLE).name(ChatColor.RESET + MenuText.FEATURE_NAME).description(lore).build(), ImmutableMap.of(ClickType.LEFT, p -> {
                build.setFeatured(!build.featured());
                updateFeatured(build, featureSlot);
            }));
        }
    }
}
