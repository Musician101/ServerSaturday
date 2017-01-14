package com.campmongoose.serversaturday.spigot.menu.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.uuid.UUIDUtils;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.menu.AbstractSpigotChestMenu;
import com.campmongoose.serversaturday.spigot.menu.anvil.page.JumpToPage;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmissions;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AllSubmissionsMenu extends AbstractSpigotChestMenu {

    private final int page;

    public AllSubmissionsMenu(Player player, int page, AbstractSpigotChestMenu prevMenu) {
        super(Bukkit.createInventory(null, 54, MenuText.ALL_SUBMISSIONS), player, prevMenu);
        this.page = page;
    }

    @Override
    protected void build() {
        List<ItemStack> list = new ArrayList<>();
        SpigotServerSaturday.instance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().forEach(build -> {
            ItemStack itemStack = new ItemStack(Material.BOOK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(build.getName());
            itemMeta.setLore(Collections.singletonList(submitter.getName()));
            if (build.submitted() && !build.featured()) {
                itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            itemStack.setItemMeta(itemMeta);
            list.add(itemStack);
        }));

        for (int x = 0; x < 54; x++) {
            int subListPosition = x + (page - 1) * 45;
            if (x < 45 && list.size() > subListPosition) {
                ItemStack itemStack = list.get(subListPosition);
                set(x, list.get(subListPosition), player -> {
                    String submitterName = itemStack.getItemMeta().getLore().get(0);
                    SpigotSubmitter submitter = null;
                    SpigotSubmissions submissions = SpigotServerSaturday.instance().getSubmissions();
                    try {
                        submitter = submissions.getSubmitter(UUIDUtils.getUUIDOf(submitterName));
                    }
                    catch (IOException exception) {
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

                    new SubmitterMenu(player, submitter, 1, this);
                });
            }
        }

        ItemStack jumpPage = new ItemStack(Material.BOOK);
        ItemMeta jumpPageMeta = jumpPage.getItemMeta();
        jumpPageMeta.setDisplayName(MenuText.JUMP_PAGE);
        jumpPage.setItemMeta(jumpPageMeta);
        set(45, jumpPage, player -> new JumpToPage(player, this, null));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.setDisplayName(MenuText.PREVIOUS_PAGE);
        prevPage.setItemMeta(prevPageMeta);
        set(49, prevPage, player -> {
            if (page - 1 < 1) {
                new AllSubmissionsMenu(player, page - 1, prevMenu);
            }
        });

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(MenuText.NEXT_PAGE);
        set(51, nextPage, player -> {
            if (page + 1 > Integer.MAX_VALUE) {
                new AllSubmissionsMenu(player, page + 1, prevMenu);
            }
        });

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MenuText.BACK);
        back.setItemMeta(backMeta);
        set(53, back, player -> {
            if (prevMenu == null) {
                close();
            }
            else {
                prevMenu.open();
            }
        });
    }
}
