package com.campmongoose.serversaturday.spigot.menu;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.menu.AbstractChestMenu;
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
import org.bukkit.entity.HumanEntity;
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

public abstract class AbstractSpigotChestMenu extends AbstractChestMenu<String, Inventory, AbstractSpigotChestMenu, Player, ItemStack, Material> implements Listener {

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

    public AbstractSpigotChestMenu(@Nonnull Inventory inventory, @Nonnull Player player,@Nullable AbstractSpigotChestMenu prevMenu) {
        this(inventory, player, prevMenu,false);
    }

    public AbstractSpigotChestMenu(@Nonnull Inventory inventory, @Nonnull Player player,@Nullable AbstractSpigotChestMenu prevMenu, boolean manualOpen) {
        super(inventory, player, prevMenu);
        SpigotServerSaturday.instance().getServer().getPluginManager().registerEvents(this, SpigotServerSaturday.instance());
        if (!manualOpen) {
            Bukkit.getScheduler().runTaskLater(SpigotServerSaturday.instance(), this::open, 1);
        }
    }

    @Override
    protected void close() {
        HandlerList.unregisterAll(this);
        if (prevMenu != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotServerSaturday.instance(), prevMenu::open);
        }
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

    @Override
    protected void setBackButton(int slot, @Nonnull Material itemType) {
        ItemStack itemStack = createItem(itemType, ChatColor.RED + MenuText.BACK, MenuText.BACK_DESC);
        set(slot, itemStack, HumanEntity::closeInventory);
    }
}
