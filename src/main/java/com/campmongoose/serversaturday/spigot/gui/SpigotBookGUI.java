package com.campmongoose.serversaturday.spigot.gui;

import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.campmongoose.serversaturday.spigot.submission.SpigotBuild;
import com.campmongoose.serversaturday.spigot.submission.SpigotSubmitter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpigotBookGUI implements Listener {

    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        Material material = itemStack.getType();
        if (material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK) {
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasLore()) {
                    return itemMeta.getLore().contains(LORE_IDENTIFIER);
                }
            }
        }

        return false;
    };
    private final int bookSlot;
    private final Consumer<List<String>> consumer;
    private final Player player;

    public SpigotBookGUI(Player player, SpigotBuild build, List<String> originalPages, Consumer<List<String>> action) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.consumer = action;
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setPages(originalPages);
        List<String> lore = bookMeta.hasLore() ? bookMeta.getLore() : new ArrayList<>();
        lore.add(LORE_IDENTIFIER);
        bookMeta.setLore(lore);
        book.setItemMeta(bookMeta);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, (SpigotServerSaturday) SpigotServerSaturday.instance());
    }

    public static boolean isEditing(Player player) {
        return !Stream.of(player.getInventory().getContents()).filter(Objects::nonNull).filter(BOOK_FILTER).collect(Collectors.toList()).isEmpty();
    }

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(isEditing((Player) event.getWhoClicked(), event.getCurrentItem()));
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        event.setCancelled(isEditing(event.getPlayer(), event.getItemDrop().getItemStack()));
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        if (oldMeta.hasLore() && oldMeta.getLore().contains(LORE_IDENTIFIER)) {
            ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            itemStack.setItemMeta(meta);
            Player player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                consumer.accept(meta.getPages());
                remove();
            }
        }
    }

    private boolean isEditing(Player player, ItemStack itemStack) {
        return player.getUniqueId().equals(this.player.getUniqueId()) && itemStack != null && BOOK_FILTER.test(itemStack);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isEditing(player, inv.getItem(bookSlot))) {
            remove();
        }
    }

    private void remove() {
        player.getInventory().setItem(bookSlot, null);
        HandlerList.unregisterAll(this);
    }

    public static void openWrittenBook(@Nonnull Player player, @Nonnull SpigotBuild build, @Nonnull SpigotSubmitter submitter) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(submitter.getName());
        bookMeta.setPages(build.getDescription());
        bookMeta.setTitle(build.getName());
        book.setItemMeta(bookMeta);
        ItemStack old = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        try {
            OPEN_BOOK.invoke(GET_HANDLE.invoke(player), AS_NMS_COPY.invoke(null, book), MAIN_HAND);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        player.getInventory().setItemInMainHand(old);
    }

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Method AS_NMS_COPY;
    private static Method GET_HANDLE;
    private static Method OPEN_BOOK;
    private static Object MAIN_HAND;

    static {
        try {
            Class<?> nmsItemStack = Class.forName("net.minecraft.server." + VERSION + ".ItemStack");
            Class<?> enumHand = Class.forName("net.minecraft.server." + VERSION + ".EnumHand");
            AS_NMS_COPY = Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class);
            OPEN_BOOK = Class.forName("net.minecraft.server." + VERSION + ".EntityPlayer").getDeclaredMethod("a", nmsItemStack, enumHand);
            GET_HANDLE = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer").getDeclaredMethod("getHandle");
            MAIN_HAND = enumHand.getEnumConstants()[0];
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
