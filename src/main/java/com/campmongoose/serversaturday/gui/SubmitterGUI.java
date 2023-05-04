package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.Reference.Messages;
import com.campmongoose.serversaturday.Reference.Permissions;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.musician101.musigui.spigot.SpigotTextInput;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;
import static io.musician101.musigui.spigot.chest.SpigotIconUtil.setLore;

public class SubmitterGUI extends ServerSaturdayChestGUI {

    private int page = 1;

    public SubmitterGUI(@Nonnull Submitter submitter, @Nonnull Player player) {
        super(player, MenuText.submitterMenu(submitter), 54);
        updateSlots(submitter);
    }

    private void updateSlots(@Nonnull Submitter submitter) {
        List<Build> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(Build::getName));
        IntStream.range(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Build build = builds.get(index);
                String name = (build.submitted() && !build.featured() ? ChatColor.GREEN : ChatColor.RED) + build.getName();
                ItemStack itemStack = setLore(customName(new ItemStack(Material.BOOK), name), "By " + submitter.getName());
                setButton(x, itemStack, Map.of(ClickType.LEFT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueId())) {
                        new EditBuildGUI(build, submitter, p);
                        return;
                    }

                    new ViewBuildGUI(build, submitter, p);
                }, ClickType.RIGHT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueId())) {
                        builds.remove(index);
                        updateSlots(submitter);
                    }
                }));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (player.getUniqueId().equals(submitter.getUUID()) && player.hasPermission(Permissions.SUBMIT)) {
            setButton(48, customName(new ItemStack(Material.EMERALD_BLOCK), MenuText.NEW_BUILD), ClickType.LEFT, p -> {
                p.closeInventory();
                p.sendMessage(Messages.SET_BUILD_NAME);
                new SpigotTextInput(ServerSaturday.getInstance(), p) {

                    @Override
                    protected void process(Player player, String s) {
                        if (submitter.getBuild(s).isPresent()) {
                            player.sendMessage(Messages.BUILD_ALREADY_EXISTS);
                            return;
                        }

                        Build build = submitter.newBuild(s, player.getLocation());
                        new EditBuildGUI(build, submitter, player);
                    }
                };
            });

            setButton(50, customName(new ItemStack(Material.BARRIER), "Back"), ClickType.LEFT, SubmittersGUI::new);
        }
        else {
            removeButton(48);
            removeButton(50);
            setButton(49, customName(new ItemStack(Material.BARRIER), "Back"), ClickType.LEFT, SubmittersGUI::new);
        }

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, customName(new ItemStack(Material.ARROW), MenuText.PREVIOUS_PAGE), ClickType.LEFT, p -> {
                page--;
                updateSlots(submitter);
            });
        }

        int maxPage = Double.valueOf(Math.ceil(builds.size() / 45d)).intValue();
        if (page >= maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, customName(new ItemStack(Material.ARROW), MenuText.NEXT_PAGE), ClickType.LEFT, p -> {
                page++;
                updateSlots(submitter);
            });
        }
    }
}
