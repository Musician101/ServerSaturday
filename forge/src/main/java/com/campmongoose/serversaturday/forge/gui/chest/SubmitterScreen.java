package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.item.Items;
import org.bukkit.event.inventory.ClickType;

//TODO replace with Forge GUIs
@Deprecated
public class SubmitterScreen extends SSForgeScreen {

    private int page = 1;

    public SubmitterScreen(@Nonnull ForgeSubmitter submitter) {
        super(MenuText.submitterMenu(submitter));
        updateSlots(submitter);
        if (player.getUniqueID().equals(submitter.getUUID())) {
            setButton(51, ForgeIconBuilder.of(Items.EMERALD_BLOCK, MenuText.NEW_BUILD), ImmutableMap.of(ClickType.LEFT, p -> {

            }));
        }
        setButton(50, ForgeIconBuilder.of(Items.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, SubmittersScreen::new));
    }

    private void updateSlots(@Nonnull ForgeSubmitter submitter) {
        List<ForgeBuild> builds = submitter.getBuilds();
        builds.sort(Comparator.comparing(ForgeBuild::getName));
        IntStream.of(0, 45).forEach(x -> {
            try {
                int index = x + (page - 1) * 45;
                ForgeBuild build = builds.get(index);
                setButton(x, build.getMenuRepresentation(submitter), ImmutableMap.of(ClickType.LEFT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueID())) {
                        new EditBuildScreen(build, submitter, p);
                        return;
                    }

                    new ViewBuildScreen(build, submitter, p);
                }, ClickType.RIGHT, p -> {
                    if (submitter.getUUID().equals(p.getUniqueID())) {
                        builds.remove(index);
                        updateSlots(submitter);
                    }
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
                updateSlots(submitter);
            }));
        }

        int maxPage = Double.valueOf(Math.ceil(builds.size() / 45d)).intValue();
        if (page < maxPage) {
            removeButton(53);
        }
        else {
            setButton(53, ForgeIconBuilder.of(Items.ARROW, MenuText.NEXT_PAGE), ImmutableMap.of(ClickType.LEFT, p -> {
                page++;
                updateSlots(submitter);
            }));
        }
    }
}
