package com.campmongoose.serversaturday.forge.gui;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

//TODO being replaced by Forge GUIs
@Deprecated
public class ForgeBookGUI implements Listener {

    private static final String IDENTIFIER = "\\_o<";
    private static final NamespacedKey KEY = new NamespacedKey(ForgeServerSaturday.getInstance(), Reference.ID);
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        Material material = itemStack.getItem();
        if (material == Items.WRITABLE_BOOK || material == Items.WRITTEN_BOOK) {
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    String identifier = itemMeta.getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
                    return Objects.equals(identifier, IDENTIFIER);
                }
            }
        }

        return false;
    };
    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Method AS_NMS_COPY;
    private static Method GET_HANDLE;
    private static Object MAIN_HAND;
    private static Method OPEN_BOOK;

    static {
        try {
            Class<?> nmsItemStack = Class.forName("net.minecraft.server." + VERSION + ".ItemStack");
            Class<?> enumHand = Class.forName("net.minecraft.server." + VERSION + ".EnumHand");
            AS_NMS_COPY = Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class);
            OPEN_BOOK = Class.forName("net.minecraft.server." + VERSION + ".EntityPlayer").getDeclaredMethod("openBook", nmsItemStack, enumHand);
            GET_HANDLE = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer").getDeclaredMethod("getHandle");
            MAIN_HAND = enumHand.getEnumConstants()[0];
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private final int bookSlot;
    private final BiConsumer<ServerPlayerEntity, List<String>> consumer;
    private final ServerPlayerEntity player;

    public ForgeBookGUI(ServerPlayerEntity player, ForgeBuild build, List<String> originalPages, BiConsumer<ServerPlayerEntity, List<String>> action) {
        this.player = player;
        this.bookSlot = player.getInventory().getHeldItemSlot();
        this.consumer = action;
        ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setDisplayName(build.getName());
        bookMeta.setPages(originalPages);
        PersistentDataContainer container = bookMeta.getPersistentDataContainer();
        container.set(KEY, PersistentDataType.STRING, IDENTIFIER);
        book.setItemMeta(bookMeta);
        player.getInventory().setItemInMainHand(book);
        Bukkit.getServer().getPluginManager().registerEvents(this, ForgeServerSaturday.getInstance());
    }

    public static boolean isEditing(ServerPlayerEntity player) {
        return Stream.of(player.getInventory().getContents()).filter(Objects::nonNull).anyMatch(BOOK_FILTER);
    }

    public static void openWrittenBook(@Nonnull ServerPlayerEntity player, @Nonnull ForgeBuild build, @Nonnull ForgeSubmitter submitter) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
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

    @EventHandler
    public void clickBook(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof ServerPlayerEntity) {
            event.setCancelled(isEditing((ServerPlayerEntity) event.getWhoClicked(), event.getCurrentItem()));
        }
    }

    @EventHandler
    public void dropBook(PlayerDropItemEvent event) {
        event.setCancelled(isEditing(event.getPlayer(), event.getItemDrop().getItemStack()));
    }

    @EventHandler
    public void editBook(PlayerEditBookEvent event) {
        BookMeta oldMeta = event.getPreviousBookMeta();
        String identifier = oldMeta.getPersistentDataContainer().get(KEY, PersistentDataType.STRING);
        if (Objects.equals(identifier, IDENTIFIER)) {
            ItemStack itemStack = new ItemStack(Items.WRITABLE_BOOK);
            oldMeta.getPersistentDataContainer();
            BookMeta meta = event.getNewBookMeta();
            meta.setLore(oldMeta.getLore());
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, IDENTIFIER);
            itemStack.setItemMeta(meta);
            ServerPlayerEntity player = event.getPlayer();
            if (isEditing(player, itemStack)) {
                consumer.accept(player, meta.getPages());
                event.setCancelled(true);
                remove();
            }
        }
    }

    private boolean isEditing(ServerPlayerEntity player, ItemStack itemStack) {
        return player.getUniqueID().equals(this.player.getUniqueID()) && itemStack != null && BOOK_FILTER.test(itemStack);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isEditing(player, inv.getItem(bookSlot))) {
            remove();
        }
    }

    private void remove() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ForgeServerSaturday.getInstance(), () -> {
            player.getInventory().setItem(bookSlot, null);
            HandlerList.unregisterAll(this);
        });
    }
}
