package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.AbstractDescriptionChangeHandler;
import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class SpigotDescriptionChangeHandler extends AbstractDescriptionChangeHandler<SpongeBuild, ClickInventoryEvent, DropItemEvent.Dispense, InteractBlockEvent, ItemStack, DataContainer, UUID, ChangeDataHolderEvent.ValueChange>
{
    @SuppressWarnings("WeakerAccess")
    public SpigotDescriptionChangeHandler()
    {
        super();
        Sponge.getEventManager().registerListeners(SpongeServerSaturday.instance(), this);
    }

    @Override
    protected ItemStack getBook(UUID uuid, SpongeBuild build)
    {
        //noinspection OptionalGetWithoutIsPresent
        return ItemStack.builder().itemType(ItemTypes.WRITABLE_BOOK)
                .add(Keys.BOOK_AUTHOR, Text.of(Sponge.getServer().getPlayer(uuid).get().getName()))//NOSONAR
                .add(Keys.DISPLAY_NAME, Text.of(build.getName()))
                .add(Keys.ITEM_LORE, Collections.singletonList(Text.of(Reference.DUCK)))
                .add(Keys.BOOK_PAGES, build.getDescription().stream().map(Text::of).collect(Collectors.toList())).build();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public void add(UUID uuid, SpongeBuild build)
    {
        Player player = Sponge.getServer().getPlayer(uuid).get();//NOSONAR
        builds.put(uuid, build);
        itemStacks.put(uuid, player.getItemInHand().isPresent() ? player.getItemInHand().get() : null);
        player.setItemInHand(getBook(uuid, build));
        taskIds.put(uuid, Task.builder().delayTicks(100).execute(() -> player.setItemInHand(itemStacks.get(uuid))).submit(SpongeServerSaturday.instance()).getUniqueId());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    protected void remove(UUID uuid)
    {
        Player player = Sponge.getServer().getPlayer(uuid).get();//NOSONAR
        player.setItemInHand(null);
        if (itemStacks.containsKey(uuid))
        {
            ItemStack itemStack = itemStacks.remove(uuid);
            if (itemStack != null)
            {
                World world = player.getWorld();
                Item itemEntity = (Item) world.createEntity(EntityTypes.ITEM, player.getLocation().getPosition()).get();
                world.spawnEntity(itemEntity, Cause.of(NamedCause.source(SpongeServerSaturday.instance())));
            }
        }

        if (builds.containsKey(uuid))
            builds.remove(uuid);

        if (builds.containsKey(uuid))
        {
            Optional<Task> taskOptional = Sponge.getScheduler().getTaskById(taskIds.remove(uuid));
            if (taskOptional.isPresent())
                taskOptional.get().cancel();
        }
    }

    @Override
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    protected boolean isSameBook(DataContainer book, UUID uuid, SpongeBuild build)
    {
        return book.equals(getBookData(getBook(uuid, build)));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private DataContainer getBookData(ItemStack itemStack)
    {
        DataContainer dc = new MemoryDataContainer();
        dc.set(Keys.BOOK_AUTHOR, itemStack.get(Keys.BOOK_AUTHOR).get());//NOSONAR
        dc.set(Keys.BOOK_PAGES, itemStack.get(Keys.BOOK_PAGES).get());
        dc.set(Keys.ITEM_LORE, itemStack.get(Keys.ITEM_LORE).get());
        dc.set(Keys.DISPLAY_NAME, itemStack.get(Keys.DISPLAY_NAME).get());
        return dc;
    }

    @Override
    @Listener
    public void bookInteract(InteractBlockEvent event)//NOSONAR
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        Player player = playerOptional.get();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid))
            return;

        Optional<ItemStack> itemStackOptional = player.getItemInHand();
        if (!itemStackOptional.isPresent())
            return;

        ItemStack itemStack = itemStackOptional.get();
        if (itemStack.getItem() != ItemTypes.WRITABLE_BOOK)
            return;

        if (!isSameBook(getBookData(itemStack), uuid, builds.get(uuid)))
            return;

        Optional<Task> taskOptional = Sponge.getScheduler().getTaskById(taskIds.remove(uuid));
        if (taskOptional.isPresent())
            taskOptional.get().cancel();
    }

    @Override
    @Listener
    public void clickBook(ClickInventoryEvent event)
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        Player player = playerOptional.get();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getCursorTransaction().getOriginal().createStack();
        if (itemStack.getItem() != ItemTypes.WRITABLE_BOOK)
            return;

        if (!isSameBook(getBookData(itemStack), uuid, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @Override
    @Listener
    public void dropBook(DropItemEvent.Dispense event)
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        Player player = playerOptional.get();
        UUID uuid = player.getUniqueId();
        //noinspection OptionalGetWithoutIsPresent
        ItemStack itemStack = event.getEntities().get(0).get(Keys.REPRESENTED_ITEM).get().createStack();//NOSONAR
        if (itemStack.getItem() != ItemTypes.WRITABLE_BOOK)
            return;

        if (!isSameBook(getBookData(itemStack), uuid, builds.get(uuid)))
            return;

        event.setCancelled(true);
    }

    @Override
    @Listener
    public void editBook(ChangeDataHolderEvent.ValueChange event)//NOSONAR
    {
        Optional<Player> playerOptional = event.getCause().first(Player.class);
        if (!playerOptional.isPresent())
            return;

        Player player = playerOptional.get();
        UUID uuid = player.getUniqueId();
        if (!containsPlayer(uuid))
            return;

        SpongeBuild build = builds.get(uuid);
        DataView dv = event.getTargetHolder().toContainer();
        if (!dv.contains(DataQuery.of("ItemType")) || ItemStack.builder().fromContainer(dv).build().getItem() != ItemTypes.WRITABLE_BOOK)
            return;

        if (!isSameBook(dv.getContainer(), uuid, build))
            return;

        SpongeSubmitter submitter = SpongeServerSaturday.instance().getSubmissions().getSubmitter(uuid);
        List<String> list = new ArrayList<>();
        //noinspection OptionalGetWithoutIsPresent
        for (Object obj : dv.getList(Keys.BOOK_PAGES.getQuery()).get())//NOSONAR
        {
            Text line = (Text) obj;
            list.add(line.toPlain());
        }

        submitter.updateBuildDescription(build, list);
        build.openMenu(submitter, player.getUniqueId());
        remove(uuid);
    }
}
