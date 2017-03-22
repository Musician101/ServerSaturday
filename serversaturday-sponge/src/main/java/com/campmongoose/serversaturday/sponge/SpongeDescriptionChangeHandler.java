package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.sponge.menu.chest.BuildGUI;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SpongeDescriptionChangeHandler {

    private final Map<UUID, SpongeBuild> builds = new HashMap<>();

    public SpongeDescriptionChangeHandler() {
        super();
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    public void add(Player player, SpongeBuild build) {
        if (player.getInventory().offer(getBook(player, build)).getType() != InventoryTransactionResult.Type.SUCCESS) {
            player.sendMessage(Text.builder(Messages.PREFIX + "Sorry, but it seems an error occurred. Is your inventory full?").color(TextColors.RED).build());
            return;
        }

        builds.put(player.getUniqueId(), build);
    }

    public boolean containsPlayer(Player player) {
        return builds.containsKey(player.getUniqueId());
    }

    @Listener
    public void dropBook(DropItemEvent.Dispense event, @First Player player) {
        UUID uuid = player.getUniqueId();
        event.getEntities().forEach(entity -> {
            if (entity.getType() == EntityTypes.ITEM) {
                Item item = (Item) entity;
                entity.get(Keys.REPRESENTED_ITEM).filter(itemStackSnapshot -> item.getType() == ItemTypes.WRITABLE_BOOK || item.getType() == ItemTypes.WRITTEN_BOOK)
                        .ifPresent(itemStackSnapshot -> {
                            SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(player);
                            SpongeBuild build = builds.remove(uuid);
                            if (!isSameBook(itemStackSnapshot.toContainer(), player, build)) {
                                return;
                            }

                            submitter.updateBuildDescription(build, itemStackSnapshot.get(Keys.BOOK_PAGES).orElse(Collections.emptyList()).stream().map(Text::toPlain).collect(Collectors.toList()));
                            entity.remove();
                            new BuildGUI(build, submitter, player, null);
                        });
            }
        });
    }

    private ItemStack getBook(Player player, SpongeBuild build) {
        return ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK)
                .add(Keys.BOOK_AUTHOR, Text.of(player.getName()))
                .add(Keys.DISPLAY_NAME, Text.of(build.getName()))
                .add(Keys.ITEM_LORE, Arrays.asList(Text.of("Just toss me onto the ground when you're done."), Text.of(Reference.DUCK)))
                .add(Keys.BOOK_PAGES, build.getDescription().stream().map(Text::of).collect(Collectors.toList())).build();
    }

    private DataContainer getBookData(ItemStack itemStack) {
        DataContainer dc = new MemoryDataContainer();
        itemStack.get(Keys.BOOK_AUTHOR).ifPresent(text -> dc.set(Keys.BOOK_AUTHOR, text));
        itemStack.get(Keys.BOOK_PAGES).ifPresent(text -> dc.set(Keys.BOOK_PAGES, text));
        itemStack.get(Keys.ITEM_LORE).ifPresent(text -> dc.set(Keys.ITEM_LORE, text));
        itemStack.get(Keys.DISPLAY_NAME).ifPresent(text -> dc.set(Keys.DISPLAY_NAME, text));
        return dc;
    }

    private boolean isSameBook(DataContainer book, Player player, SpongeBuild build) {
        DataContainer bookData = getBookData(getBook(player, build));
        boolean authorMatch = book.getObject(Keys.BOOK_AUTHOR.getQuery(), Text.class)
                .map(author -> {
                    Optional<Text> a = bookData.getObject(Keys.BOOK_AUTHOR.getQuery(), Text.class);
                    return a.isPresent() && author.equals(a.get());
                }).orElse(false);
        boolean loreMatch = book.getObjectList(Keys.ITEM_LORE.getQuery(), Text.class)
                .map(lore -> {
                    Optional<List<Text>> l = bookData.getObjectList(Keys.ITEM_LORE.getQuery(), Text.class);
                    return l.isPresent() && lore.equals(l.get());
                }).orElse(false);
        boolean nameMatch = book.getObject(Keys.DISPLAY_NAME.getQuery(), Text.class)
                .map(name -> {
                    Optional<Text> n = bookData.getObject(Keys.DISPLAY_NAME.getQuery(), Text.class);
                    return n.isPresent() && name.equals(n.get());
                }).orElse(false);

        return authorMatch && loreMatch && nameMatch;
    }
}
