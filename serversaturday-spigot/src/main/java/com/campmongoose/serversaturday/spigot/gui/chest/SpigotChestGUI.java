package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.gui.chest.ChestGUI;
import com.campmongoose.serversaturday.common.gui.chest.GUIButton;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public final class SpigotChestGUI extends ChestGUI<ClickType, Inventory, Player, ItemStack, String> implements Listener {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Field activeContainer;
    private static Field defaultContainer;
    private static Method getHandle;
    private static Method handleInventoryCloseEvent;

    static {
        try {
            final Class<?> entityHuman = Class.forName("net.minecraft.server." + SERVER_VERSION + ".EntityHuman");
            handleInventoryCloseEvent = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".event.CraftEventFactory").getDeclaredMethod("handleInventoryCloseEvent", entityHuman);
            getHandle = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".entity.CraftPlayer").getDeclaredMethod("getHandle");
            defaultContainer = entityHuman.getDeclaredField("defaultContainer");
            activeContainer = entityHuman.getDeclaredField("activeContainer");
        }
        catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    SpigotChestGUI(@Nonnull Player player, @Nonnull String name, int size, @Nonnull List<GUIButton<ClickType, Player, ItemStack>> buttons, int page, boolean manualOpen) {
        super(parseInventory(player, name, size), name, player, buttons, page, manualOpen);
    }

    public static SpigotChestGUIBuilder builder() {
        return new SpigotChestGUIBuilder();
    }

    private static Inventory parseInventory(Player player, String name, int size) {
        return name == null ? Bukkit.createInventory(player, size) : Bukkit.createInventory(player, size, name);
    }

    @Override
    public final void close() {
        player.closeInventory();
    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(name) && view.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            event.setCancelled(true);
            buttons.stream().filter(button -> button.getSlot() == event.getRawSlot() && button.getClickType() == event.getClick()).findFirst().flatMap(GUIButton::getAction).ifPresent(action -> action.accept((Player) event.getWhoClicked()));
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals(name) && view.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public final void onInventoryItemDrag(InventoryDragEvent event) {
        InventoryView view = event.getView();
        event.setCancelled(view.getTitle().equals(name) && view.getPlayer().getUniqueId().equals(player.getUniqueId()));
    }

    @Override
    public final void open() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), () -> {
            try {
                inventory.clear();
                buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItemStack()));

                final Object entityHuman = getHandle.invoke(player);
                handleInventoryCloseEvent.invoke(null, entityHuman);
                activeContainer.set(entityHuman, defaultContainer.get(entityHuman));

                player.openInventory(inventory);
                Bukkit.getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
