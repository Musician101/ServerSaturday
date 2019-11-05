package com.campmongoose.serversaturday.sponge.gui;

import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.data.manipulator.mutable.UUIDData;
import com.campmongoose.serversaturday.sponge.event.AffectBookEvent;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;

public class SpongeBookGUI {

    private static final String LORE_IDENTIFIER = "\\_o<";
    private static final Predicate<ItemStack> BOOK_FILTER = itemStack -> {
        ItemType itemType = itemStack.getType();
        return (itemType == ItemTypes.WRITABLE_BOOK || itemType == ItemTypes.WRITTEN_BOOK) && itemStack.get(Keys.ITEM_LORE).map(lore -> lore.stream().map(Text::toPlain).collect(Collectors.toList())).filter(lore -> lore.contains(LORE_IDENTIFIER)).isPresent();

    };
    private final Consumer<List<Text>> action;
    private final int bookSlot;
    private final Player player;

    public SpongeBookGUI(Player player, SpongeBuild build, List<Text> originalPages, Consumer<List<Text>> action) {
        this.player = player;
        Hotbar hotbar = player.getInventory().query(QueryOperationTypes.TYPE.of(Hotbar.class));
        this.bookSlot = hotbar.getSelectedSlotIndex();
        this.action = action;
        ItemStack book = ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK).quantity(1)
                .add(Keys.BOOK_AUTHOR, Text.of(player.getName()))
                .add(Keys.DISPLAY_NAME, Text.of(build.getName()))
                .add(Keys.BOOK_PAGES, originalPages.stream().map(Text::of).collect(Collectors.toList()))
                .add(Keys.ITEM_LORE, Collections.singletonList(Text.of(LORE_IDENTIFIER))).build();
        hotbar.set(new SlotIndex(bookSlot), book);
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance().get(), this);
    }

    public static boolean isEditing(Player player) {
        return !(StreamSupport.stream(player.getInventory().spliterator(), false).map(Inventory::peek).filter(optional -> optional.filter(BOOK_FILTER).isPresent()).map(Optional::get).count() == 0);
    }

    @Listener
    public void clickBook(ClickInventoryEvent event, @Getter("getCursorTransaction") Transaction<ItemStackSnapshot> transaction, @First Player player) {
        event.setCancelled(isEditing(player) && transaction.getFinal().createStack().get(UUIDData.class).isPresent());
    }

    @Listener
    public void editBook(AffectBookEvent event, @Getter("getTransaction") Transaction<ItemStackSnapshot> transaction, @Getter("getTargetEntity") Player player) {
        ItemStack finalStack = transaction.getFinal().createStack();
        if (finalStack.get(UUIDData.class).isPresent()) {
            if (isEditing(player)) {
                action.accept(finalStack.get(Keys.BOOK_PAGES).orElse(Collections.emptyList()));
                remove();
            }
        }
    }

    @Listener
    public void playerQuit(ClientConnectionEvent.Disconnect event, @First Player player) {
        if (isEditing(player)) {
            remove();
        }
    }

    private void remove() {
        ((Hotbar) player.getInventory().query(QueryOperationTypes.TYPE.of(Hotbar.class))).set(new SlotIndex(bookSlot), ItemStack.empty());
        Sponge.getEventManager().unregisterListeners(this);
    }
}
