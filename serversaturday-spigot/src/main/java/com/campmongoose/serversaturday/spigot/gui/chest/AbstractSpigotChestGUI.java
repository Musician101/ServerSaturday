package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.gui.AbstractChestGUI;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractSpigotChestGUI extends AbstractChestGUI<String, Inventory, AbstractSpigotChestGUI, Player, ItemStack, Material> implements Listener {

    private static Field activeContainer;
    private static Field defaultContainer;
    private static Method getHandle;
    private static Method handleInventoryCloseEvent;

    static {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            String craft = "org.bukkit.craftbukkit." + version + ".";
            String nms = "net.minecraft.server." + version + ".";
            final Class<?> entityHuman = Class.forName(nms + "EntityHuman");
            handleInventoryCloseEvent = Class.forName(craft + "event.CraftEventFactory").getDeclaredMethod("handleInventoryCloseEvent", entityHuman);
            getHandle = Class.forName(craft + "entity.CraftPlayer").getDeclaredMethod("getHandle");
            defaultContainer = entityHuman.getDeclaredField("defaultContainer");
            activeContainer = entityHuman.getDeclaredField("activeContainer");
        }
        catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    public AbstractSpigotChestGUI(@Nonnull Inventory inventory, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu) {
        this(inventory, player, prevMenu, false);
    }

    public AbstractSpigotChestGUI(@Nonnull Inventory inventory, @Nonnull Player player, @Nullable AbstractSpigotChestGUI prevMenu, boolean manualOpen) {
        super(inventory, player, prevMenu);
        if (!manualOpen) {
            Bukkit.getScheduler().runTaskLater(SpigotServerSaturday.instance(), this::open, 1);
        }
    }

    @Override
    protected void close() {
        HandlerList.unregisterAll(this);
    }

    private boolean isSameInventory(Inventory inventory) {
        return inventory.getName().equals(this.inventory.getName()) && inventory.getHolder().equals(player);
    }

    @Nonnull
    @Override
    protected ItemStack createItem(@Nonnull Material itemType, @Nonnull String name, @Nonnull String... description) {
        ItemStack itemStack = new ItemStack(itemType);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(description));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (isSameInventory(event.getClickedInventory())) {
            event.setCancelled(true);
            if (buttons.containsKey(event.getRawSlot())) {
                buttons.get(event.getRawSlot()).accept((Player) event.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (isSameInventory(event.getInventory())) {
            close();
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(isSameInventory(event.getInventory()));
    }

    @Override
    public void open() {
        try {
            inventory.clear();
            build();

            final Object entityHuman = getHandle.invoke(player);
            handleInventoryCloseEvent.invoke(null, entityHuman);
            activeContainer.set(entityHuman, defaultContainer.get(entityHuman));

            player.openInventory(inventory);
            Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delayedOpen() {
        Bukkit.getScheduler().runTaskLater(SpigotServerSaturday.instance(), this::open, 1L);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void setBackButton(int slot, @Nonnull Material itemType) {
        ItemStack itemStack = createItem(itemType, ChatColor.RED + MenuText.BACK, MenuText.BACK_DESC.toArray(new String[0]));
        set(slot, itemStack, player -> {
            player.closeInventory();
            if (prevMenu != null) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), prevMenu::open);
            }
        });
    }
}
