package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import org.bukkit.event.inventory.ClickType;
//TODO replace with Forge GUIs
@Deprecated
public class AllSubmissionsScreen extends SSForgeScreen {

    private int page = 1;

    public AllSubmissionsScreen(@Nonnull ServerPlayerEntity player) {
        super(MenuText.ALL_SUBMISSIONS);
        updateSlots();
        setButton(50, ForgeIconBuilder.of(Items.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, ServerPlayerEntity::closeInventory));
    }

    private void updateSlots() {
        Multimap<ForgeBuild, ForgeSubmitter> map = TreeMultimap.create(Comparator.comparing(ForgeBuild::getName), Comparator.comparing(ForgeSubmitter::getName));
        ForgeServerSaturday.getInstance().getSubmissions().getSubmitters().forEach(submitter -> submitter.getBuilds().stream().filter(build -> !build.featured() && build.submitted()).forEach(build -> map.put(build, submitter)));
        List<ForgeBuild> builds = new ArrayList<>(map.keys());
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                ForgeBuild build = builds.get(index);
                ForgeSubmitter submitter = map.get(build).iterator().next();
                map.remove(build, submitter);
                setButton(x, build.getMenuRepresentation(submitter), ImmutableMap.of(ClickType.LEFT, p -> {
                    if (p.getUniqueID().equals(submitter.getUUID())) {
                        new EditBuildScreen(build, submitter, p);
                        return;
                    }

                    new ViewBuildScreen(build, submitter, p);
                }));
            }
            catch (IndexOutOfBoundsException ignored) {

            }
        });

        if (page == 1) {
            removeButton(45);
        }
        else {
            setButton(45, ForgeIconBuilder.of(Items.ARROW, MenuText.PREVIOUS_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page--;
                updateSlots();
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(map.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, ForgeIconBuilder.of(Items.ARROW, MenuText.NEXT_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page++;
                updateSlots();
            }));
        }
    }
}
