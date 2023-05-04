package com.campmongoose.serversaturday.gui;

import com.campmongoose.serversaturday.Reference.MenuText;
import com.campmongoose.serversaturday.ServerSaturday;
import com.campmongoose.serversaturday.submission.Submitter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static io.musician101.musigui.spigot.chest.SpigotIconUtil.customName;

public class SubmittersGUI extends ServerSaturdayChestGUI {

    private int page = 1;

    public SubmittersGUI(@Nonnull Player player) {
        super(player, MenuText.SUBMISSIONS, 54);
        updateSlots();
        setButton(50, customName(new ItemStack(Material.BARRIER), "Back"), ClickType.LEFT, Player::closeInventory);
    }

    private void updateSlots() {
        List<Submitter> submitters = ServerSaturday.getInstance().getSubmissions().getSubmitters();
        submitters.sort(Comparator.comparing(Submitter::getName));
        IntStream.range(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                Submitter submitter = submitters.get(index);
                Material material = submitter.getBuilds().stream().anyMatch(b -> b.submitted() && !b.featured()) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;
                ItemStack itemStack = customName(new ItemStack(material), submitter.getName());
                setButton(x, itemStack, ClickType.LEFT, p -> new SubmitterGUI(submitter, p));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, customName(new ItemStack(Material.ARROW), MenuText.PREVIOUS_PAGE), ClickType.LEFT, p -> {
                page--;
                updateSlots();
            });
        }

        int maxPage = Double.valueOf(Math.ceil(submitters.size() / 45d)).intValue();
        if (page >= maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, customName(new ItemStack(Material.ARROW), MenuText.NEXT_PAGE), ClickType.LEFT, p -> {
                page++;
                updateSlots();
            });
        }
    }
}
