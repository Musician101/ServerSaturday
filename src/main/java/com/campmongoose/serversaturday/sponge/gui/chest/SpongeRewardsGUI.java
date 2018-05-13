package com.campmongoose.serversaturday.sponge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.common.gui.chest.RewardsGUI;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

public class SpongeRewardsGUI extends RewardsGUI<Inventory, Player> {

    private static ConfigurationLoader<CommentedConfigurationNode> LOADER;
    private static List<ItemStack> REWARDS;
    private static File REWARDS_FILE;

    public SpongeRewardsGUI(@Nonnull SpongeServerSaturday plugin, @Nonnull Player player) {
        super(parseInventory(plugin), player);
        player.openInventory(inventory);
    }

    public static List<ItemStack> getRewards() {
        return REWARDS;
    }

    public static void loadRewards(SpongeServerSaturday plugin, File configDir) {
        REWARDS_FILE = new File(configDir, "REWARDS.yml");
        try {
            if (!REWARDS_FILE.exists()) {
                REWARDS_FILE.createNewFile();
            }

            LOADER = HoconConfigurationLoader.builder().setFile(REWARDS_FILE).build();
            ConfigurationNode node = LOADER.load();
            IntStream.range(0, 27).mapToObj(node::getNode).filter(itemNode -> !itemNode.isVirtual()).map(DataTranslators.CONFIGURATION_NODE::translate).map(container -> ItemStack.builder().fromContainer(container).build()).forEach(REWARDS::add);

        }
        catch (IOException e) {
            plugin.getLogger().error(Messages.failedToSaveRewardsFile(REWARDS_FILE));
        }
    }

    private static Inventory parseInventory(SpongeServerSaturday plugin) {
        InventoryArchetype.Builder builder = InventoryArchetype.builder().property(new InventoryCapacity(27));
        IntStream.range(0, 27).forEach(i -> builder.with(InventoryArchetype.builder().from(InventoryArchetypes.SLOT).property(new SlotIndex(i)).build("minecraft:slot" + i, "Slot")).title(Text.of(MenuText.REWARDS)));
        return Inventory.builder().of(builder.build(plugin.getId() + ":rewards", "Rewards")).build(plugin);
    }

    @Listener
    public void onClose(InteractInventoryEvent.Close event, @First Player player) {
        Container eventInventory = event.getTargetInventory();
        if (eventInventory.getName().get().equals(inventory.getName().get()) && eventInventory.getViewers().contains(this.player)) {
            ConfigurationNode node = LOADER.createEmptyNode();
            List<ItemStack> newRewards = new ArrayList<>();
            IntStream.range(0, 27).forEach(i -> eventInventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(new SlotIndex(i))).peek().filter(itemStack -> itemStack.getType() != ItemTypes.AIR).ifPresent(itemStack -> {
                newRewards.add(itemStack);
                node.getNode(i).setValue(DataTranslators.CONFIGURATION_NODE.translate(itemStack.toContainer()));
            }));

            REWARDS = newRewards;
            try {
                LOADER.save(node);
            }
            catch (IOException e) {
                player.sendMessage(Text.of(TextColors.RED, Messages.failedToSaveRewardsFile(REWARDS_FILE)));
                REWARDS.forEach(itemStack -> {
                    World world = player.getWorld();
                    Entity entity = world.createEntity(EntityTypes.ITEM, player.getLocation().getPosition());
                    entity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
                    world.spawnEntity(entity);
                });
            }
        }
    }
}
