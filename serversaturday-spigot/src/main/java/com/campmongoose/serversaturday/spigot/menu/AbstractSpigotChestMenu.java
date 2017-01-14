package com.campmongoose.serversaturday.spigot.menu;

import com.campmongoose.serversaturday.common.menu.AbstractChestMenu;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getEntityClass;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getEventClass;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getField;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getMethod;
import static com.campmongoose.serversaturday.spigot.ReflectionUtils.getNMSClass;

public abstract class AbstractSpigotChestMenu extends AbstractChestMenu<Inventory, AbstractSpigotChestMenu, Player, ItemStack> implements Listener {

    protected static Field activeContainer;
    protected static Field defaultContainer;
    protected static Method getHandle;
    protected static Method handleInventoryCloseEvent;

    static {
        try {
            final Class<?> entityHuman = getNMSClass("EntityHuman");
            handleInventoryCloseEvent = getMethod(getEventClass("CraftEventFactory"), "handleInventoryCloseEvent", entityHuman);
            getHandle = getMethod(getEntityClass("CraftPlayer"), "getHandle");
            defaultContainer = getField(entityHuman, "defaultContainer");
            activeContainer = getField(entityHuman, "activeContainer");
        }
        catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    public AbstractSpigotChestMenu(@Nonnull Inventory inventory, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu) {
        this(inventory, player, prevMenu, false);
    }

    public AbstractSpigotChestMenu(@Nonnull Inventory inventory, @Nonnull Player player, @Nullable AbstractSpigotChestMenu prevMenu, boolean manualOpen) {
        super(inventory, player, prevMenu, manualOpen);
        SpigotServerSaturday.instance().getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
    }

    @Override
    protected void close() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player)) {
            event.setCancelled(true);
            if (buttons.containsKey(event.getRawSlot())) {
                buttons.get(event.getRawSlot()).accept((Player) event.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player)) {
            close();
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(event.getInventory().getName().equals(inventory.getName()) && event.getInventory().getHolder().equals(player));
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
    protected void set(int slot, @Nonnull ItemStack itemStack, @Nonnull Consumer<Player> consumer) {
        set(slot, itemStack);
        buttons.put(slot, consumer);
    }

    @Override
    protected void set(int slot, @Nonnull ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }
}
