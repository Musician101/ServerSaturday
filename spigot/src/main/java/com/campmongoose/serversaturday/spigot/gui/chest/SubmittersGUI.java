package com.campmongoose.serversaturday.spigot.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.submission.Submitter;
import com.campmongoose.serversaturday.spigot.SpigotServerSaturday;
import com.google.common.collect.ImmutableMap;
import io.musician101.musicianlibrary.java.minecraft.spigot.gui.chest.SpigotIconBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SubmittersGUI extends SpigotServerSaturdayChestGUI {

    private int page = 1;

    public SubmittersGUI(@Nonnull Player player) {
        super(player, MenuText.SUBMISSIONS, 54);
        updateSlots();
        setButton(50, SpigotIconBuilder.of(Material.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, HumanEntity::closeInventory));
    }

    private void updateSlots() {
        List<Submitter> submitters = SpigotServerSaturday.instance().getSubmissions().getData();
        submitters.sort(Comparator.comparing(Submitter::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Submitter submitter = submitters.get(index);
                ItemStack itemStack = SpigotIconBuilder.of(submitter.getBuilds().stream().anyMatch(b -> b.submitted() && !b.featured()) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, submitter.getName());
                setButton(x, itemStack, ImmutableMap.of(ClickType.LEFT, p -> new SubmitterGUI(submitter, p)));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, SpigotIconBuilder.of(Material.ARROW, MenuText.PREVIOUS_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page--;
                updateSlots();
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(submitters.size() / 45d)).intValue();
        if (page > maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, SpigotIconBuilder.of(Material.ARROW, MenuText.NEXT_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page++;
                updateSlots();
            }));
        }
    }
}
