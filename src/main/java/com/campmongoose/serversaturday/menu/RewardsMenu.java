package com.campmongoose.serversaturday.menu;

import com.campmongoose.serversaturday.Reference;
import com.campmongoose.serversaturday.ServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RewardsMenu implements Listener {

    private static File REWARDS_FILE;
    private static ItemStack[] rewards;
    private final Inventory inventory;
    private final Player player;

    public RewardsMenu(Player player) {
        this.player = player;
        inventory = Bukkit.createInventory(player, InventoryType.CHEST, "S. S. Rewards");
        if (rewards == null) {
            player.sendMessage(ChatColor.RED + Reference.PREFIX + "An error occurred when loading the rewards during the last start up.");
            return;
        }

        inventory.setContents(rewards);
        Bukkit.getPluginManager().registerEvents(this, ServerSaturday.instance());
        player.openInventory(inventory);
    }

    public static ItemStack[] getRewards() {
        return rewards;
    }

    public static void loadRewards() {
        REWARDS_FILE = new File(ServerSaturday.instance().getDataFolder(), "rewards.yml");
        if (!REWARDS_FILE.exists()) {
            try {
                REWARDS_FILE.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        int defaultSize = InventoryType.CHEST.getDefaultSize();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(REWARDS_FILE);
        rewards = new ItemStack[defaultSize];
        for (int i = 0; i < defaultSize; i++) {
            rewards[i] = yml.getItemStack(Integer.toString(i), null);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory eventInventory = event.getInventory();
        if (eventInventory.getName().equals(inventory.getName()) && eventInventory.getHolder().equals(player)) {
            Inventory rewardsInventory = event.getView().getTopInventory();
            YamlConfiguration yml = new YamlConfiguration();
            for (int i = 0; i < rewardsInventory.getSize(); i++) {
                rewards[i] = null;
                ItemStack itemStack = rewardsInventory.getItem(i);
                if (itemStack != null) {
                    rewards[i] = itemStack;
                    yml.set(Integer.toString(i), itemStack);
                }
            }

            try {
                yml.save(REWARDS_FILE);
                player.sendMessage(ChatColor.GOLD + Reference.PREFIX + "Rewards saved successfully.");
            }
            catch (IOException e) {
                player.sendMessage(ChatColor.RED + Reference.PREFIX + "An error occurred while trying to save the rewards. Returning contents just in case.");
                Stream.of(rewardsInventory.getContents()).filter(Objects::nonNull).forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
            }

            HandlerList.unregisterAll(this);
        }
    }
}
