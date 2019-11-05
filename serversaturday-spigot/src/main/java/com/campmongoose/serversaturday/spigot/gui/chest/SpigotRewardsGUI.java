package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.gui.chest.RewardsGUI;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class SpigotRewardsGUI extends RewardsGUI<Inventory, Player> implements Listener {

    private final static File REWARDS_FILE;
    private final static ItemStack[] rewards;

    static {
        REWARDS_FILE = new File(SpigotServerSaturday.instance().getDataFolder(), "rewards.yml");
        if (!REWARDS_FILE.exists()) {
            try {
                REWARDS_FILE.createNewFile();
            }
            catch (IOException e) {
                SpigotServerSaturday.instance().getLogger().warning(Messages.failedToWriteFile(REWARDS_FILE));
            }
        }

        int defaultSize = InventoryType.CHEST.getDefaultSize();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(REWARDS_FILE);
        rewards = new ItemStack[defaultSize];
        IntStream.range(0, defaultSize).forEach(i -> rewards[i] = yml.getItemStack(Integer.toString(i), null));
    }

    public SpigotRewardsGUI(@Nonnull Player player) {
        super(Bukkit.createInventory(player, InventoryType.CHEST, MenuText.REWARDS), player);
        inventory.setContents(rewards);
        player.openInventory(inventory);
        Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    public static ItemStack[] getRewards() {
        return rewards;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryView eventInventory = event.getView();
        if (eventInventory.getTitle().equals(MenuText.REWARDS) && eventInventory.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            Inventory rewardsInventory = event.getView().getTopInventory();
            YamlConfiguration yml = new YamlConfiguration();
            IntStream.range(0, rewardsInventory.getSize()).forEach(i -> {
                rewards[i] = null;
                ItemStack itemStack = rewardsInventory.getItem(i);
                if (itemStack != null) {
                    rewards[i] = itemStack;
                    yml.set(Integer.toString(i), itemStack);
                }
            });

            try {
                yml.save(REWARDS_FILE);
            }
            catch (IOException e) {
                player.sendMessage(ChatColor.RED + Messages.failedToSaveRewardsFile(REWARDS_FILE));
                Stream.of(rewardsInventory.getContents()).filter(Objects::nonNull).forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
            }

            HandlerList.unregisterAll(this);
        }
    }
}
