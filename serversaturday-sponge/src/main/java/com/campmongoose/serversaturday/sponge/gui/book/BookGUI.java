package com.campmongoose.serversaturday.sponge.gui.book;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.common.Mod.EventHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent.Primary;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

//The Minecraft Dev plugin for Intellij Idea highlights the event parameters as an error due to having Forge as a dependency.
//These are false positives.
public class BookGUI {

    private final Consumer<List<String>> consumer;
    private final Inventory inventory;
    private final int bookSlot;
    private final Player player;
    private static final String LORE_IDENTIFIER = "\\_o<";

    public BookGUI(Player player, SpongeBuild build, List<String> originalPages, Consumer<List<String>> consumer) {
        this.player = player;
        this.bookSlot = ((Hotbar) player.getInventory().query(Hotbar.class)).getSelectedSlotIndex();
        this.consumer = consumer;
        player.getInventory().query(Hotbar.class);
        ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).quantity(1)
                .add(Keys.BOOK_AUTHOR, Text.of(player.getName()))
                .add(Keys.DISPLAY_NAME, Text.of(build.getName()))
                .add(Keys.BOOK_PAGES, originalPages.stream().map(Text::of).collect(Collectors.toList()));

        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(9)).title(Text.of("Book Deposit"));
        for (int x = 0; x < 9; x++) {
            builder.with(InventoryArchetype.builder().with(InventoryArchetypes.SLOT).property(new SlotIndex(x)).build("minecraft:slot" + x, "Slot"));
        }

        inventory = Inventory.builder().build(SpongeServerSaturday.instance());
        for (int x = 0; x < 9; x++) {
            if (x != 4) {
                inventory.query(new SlotIndex(x)).set(ItemStack.builder().itemType(ItemTypes.GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.WHITE).add(Keys.DISPLAY_NAME, Text.of()).build());
            }
        }
    }

    public static boolean isEditing(Player player) {
        return !StreamSupport.stream(player.getInventory().query(ItemTypes.WRITABLE_BOOK, ItemTypes.WRITTEN_BOOK).spliterator(), false).map(inventory -> inventory.peek().orElse(ItemStack.empty())).filter(itemStack -> itemStack.get(Keys.ITEM_LORE).orElse(Collections.emptyList()).stream().map(Text::toPlain).collect(Collectors.toList()).contains(LORE_IDENTIFIER)).collect(Collectors.toList()).isEmpty();
    }

    private boolean isEditing(@Nonnull Player player, @Nonnull ItemStack itemStack) {
        if (player.getUniqueId().equals(this.player.getUniqueId()) && (itemStack.getItem() == ItemTypes.WRITABLE_BOOK || itemStack.getItem() == ItemTypes.WRITTEN_BOOK)) {
            return itemStack.get(Keys.ITEM_LORE).map(lore -> lore.stream().map(Text::toPlain).collect(Collectors.toList()).contains(LORE_IDENTIFIER)).orElse(false);
        }

        return false;
    }

    @EventHandler
    public void onRightClick(Primary.MainHand event, @First Player player) {
        if (isEditing(player, event.getItemStack().createStack())) {
            player.openInventory(inventory, Cause.of(NamedCause.source(SpongeServerSaturday.instance())));
        }
    }

    @EventHandler
    public void onClick(ClickInventoryEvent event, @First Player player) {
        Container container = event.getTargetInventory();
        if (container.getName().get().equals(inventory.getName().get()) && player.getUniqueId().equals(this.player.getUniqueId())) {
            ItemStack book = event.getCursorTransaction().getFinal().createStack();
            if (isEditing(player, book) && event instanceof ClickInventoryEvent.Primary) {
                book.get(Keys.BOOK_PAGES).map(pages -> pages.stream().map(Text::toPlain).collect(Collectors.toList())).ifPresent(consumer);
                remove();
            }
            else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerQuit(ClientConnectionEvent.Disconnect event, @First Player player) {
        Hotbar hotbar = player.getInventory().query(Hotbar.class);
        if (isEditing(player, hotbar.peek(new SlotIndex(bookSlot)).orElse(ItemStack.empty()))) {
            remove();
        }
    }

    private void remove() {
        ((Hotbar) player.getInventory().query(Hotbar.class)).set(new SlotIndex(bookSlot), ItemStack.empty());
        Sponge.getEventManager().unregisterListeners(this);
    }
}
