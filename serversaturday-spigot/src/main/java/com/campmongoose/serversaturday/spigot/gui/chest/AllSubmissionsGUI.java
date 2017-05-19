package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.submission.SubmissionsNotLoadedException;
import com.campmongoose.serversaturday.common.uuid.MojangAPIException;
import com.campmongoose.serversaturday.common.uuid.PlayerNotFoundException;
import com.campmongoose.serversaturday.common.uuid.UUIDCache;
import com.campmongoose.serversaturday.common.uuid.UUIDCacheException;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AllSubmissionsGUI extends AbstractSpigotPagedGUI {

    public AllSubmissionsGUI(@Nonnull Player player, int page, @Nullable AbstractSpigotChestGUI prevMenu) {
        super(Bukkit.createInventory(null, 54, MenuText.ALL_SUBMISSIONS), player, page, prevMenu);
    }

    @Override
    protected void build() {
        List<ItemStack> list = new ArrayList<>();
        SpigotServerSaturday plugin = SpigotServerSaturday.instance();
        SpigotSubmissions submissions;
        UUIDCache uuidCache;
        try {
            submissions = plugin.getSubmissions();
            uuidCache = plugin.getUUIDCache();
        }
        catch (SubmissionsNotLoadedException | UUIDCacheException e) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }

        submissions.getSubmitters().forEach(submitter ->
                list.addAll(submitter.getBuilds().stream().map(build ->
                        build.getMenuRepresentation(submitter)).collect(Collectors.toList())));

        setContents(list, (p, itemStack) ->
                player -> {
                    String submitterName = itemStack.getItemMeta().getLore().get(0);
                    SpigotSubmitter submitter = null;
                    UUID uuid;
                    try {
                        uuid = uuidCache.getUUIDOf(submitterName);
                    }
                    catch (IOException | MojangAPIException | PlayerNotFoundException e) {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + e.getMessage());
                        return;
                    }

                    if (uuid != null) {
                        submitter = submissions.getSubmitter(uuid);
                    }
                    else {
                        for (SpigotSubmitter s : submissions.getSubmitters()) {
                            if (submitterName.equals(s.getName())) {
                                submitter = s;
                            }
                        }
                    }

                    if (submitter == null) {
                        player.sendMessage(ChatColor.RED + Messages.PLAYER_NOT_FOUND);
                        return;
                    }

                    new SubmitterGUI(player, submitter, 1, this);
                });

        int maxPage = new Double(Math.ceil(list.size() / 45)).intValue();
        setJumpToPage(45, maxPage);
        setPageNavigationButton(48, MenuText.PREVIOUS_PAGE, player -> {
            if (page > 1) {
                new AllSubmissionsGUI(player, page - 1, this);
            }
        });

        setPageNavigationButton(50, MenuText.NEXT_PAGE, player -> {
            if (page < maxPage) {
                new AllSubmissionsGUI(player, page + 1, this);
            }
        });

        setBackButton(53, Material.BARRIER);
    }
}
