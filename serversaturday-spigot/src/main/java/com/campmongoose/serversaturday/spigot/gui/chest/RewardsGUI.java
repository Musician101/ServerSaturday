package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RewardsGUI extends AbstractSpigotChestGUI {

    private static File REWARDS_FILE;
    private static ItemStack[] rewards;

    public RewardsGUI(@Nonnull Player player) {
        super(Bukkit.createInventory(player, InventoryType.CHEST, MenuText.REWARDS), player, null);
    }

    public static void loadRewards() {
        REWARDS_FILE = new File(SpigotServerSaturday.instance().getDataFolder(), "rewards.yml");
        if (!REWARDS_FILE.exists()) {
            try {
                REWARDS_FILE.createNewFile();
            }
            catch (IOException e) {
                SpigotServerSaturday.instance().getLogger().warning(Messages.ioException(REWARDS_FILE));
            }
        }

        int defaultSize = InventoryType.CHEST.getDefaultSize();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(REWARDS_FILE);
        rewards = new ItemStack[defaultSize];
        for (int i = 0; i < defaultSize; i++) {
            rewards[i] = yml.getItemStack(Integer.toString(i), null);
        }
    }

    public static ItemStack[] getRewards() {
        return rewards;
    }

    @Override
    protected void build() {
        inventory.setContents(rewards);
    }

    @EventHandler
    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @EventHandler
    @Override
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
            }
            catch (IOException e) {
                player.sendMessage(ChatColor.RED + Messages.failedToSaveRewardsFile(REWARDS_FILE));
                Stream.of(rewardsInventory.getContents()).filter(Objects::nonNull).forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
            }

            close();
        }
    }

    @EventHandler
    @Override
    public void onDrag(InventoryDragEvent event) {

    }
}
