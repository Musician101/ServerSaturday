package com.campmongoose.serversaturday;

import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface Reference {

    interface Commands {

        String BUILD = "build";
        String PLAYER = "player";
    }

    interface Config {

        String BUILDS = "builds";
        String DESCRIPTION = "description";
        String FEATURED = "featured";
        String LOCATION = "location";
        String NAME = "name";
        String RESOURCE_PACK = "resource_pack";
        String REWARDS = "rewards";
        String SUBMITTED = "submitted";
        String UUID = "uuid";

    }

    interface MenuText {

        String ALL_SUBMISSIONS = "All S.S. Submissions";
        String CHANGE_DESCRIPTION_DESC = "Add or change the description to this build.";
        String CHANGE_DESCRIPTION_NAME = "Change Description";
        List<String> CHANGE_LOCATION_DESC = Arrays.asList("Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build.");
        String CHANGE_LOCATION_NAME = "Change Location";
        List<String> CHANGE_RESOURCES_PACK_DESC = Arrays.asList("Change the recommended resource", "packs for this build.");
        String CHANGE_RESOURCE_PACKS_NAME = "Change Resource Packs";
        String DESCRIPTION_DESC = "View this build's description.";
        String DESCRIPTION_NAME = "Description";
        List<String> FEATURE_DESC = Arrays.asList("Set whether this build has been covered in", "an episode of Server Saturday.");
        String FEATURE_NAME = "Feature";
        String NEW_BUILD = "New Build";
        String NEXT_PAGE = "Next Page";
        String PREVIOUS_PAGE = "Previous Page";
        String RENAME_DESC = "Rename this build.";
        String RENAME_NAME = "Rename";
        String RESOURCE_PACK_DESC = "View this build's recommended resource packs.";
        String RESOURCE_PACK_NAME = "Resource Pack";
        String SUBMISSIONS = "S. S. Submissions";
        List<String> SUBMIT_UNREADY_DESC = Arrays.asList("Add or remove your build from", "the list of ready builds.");
        String SUBMIT_UNREADY_NAME = "Submit/Unready";
        String TELEPORT_NAME = "Teleport";

        @Nonnull
        static String submitterMenu(@Nonnull Submitter submitter) {
            return submitter.getName() + "'s Builds";
        }

        @Nonnull
        static List<String> teleportDesc(@Nonnull String name, int x, int y, int z) {
            return Arrays.asList("Click to teleport.", "- World: " + name, "- X: " + x, "- Y: " + y, "- Z: " + z);
        }
    }

    interface Messages {

        String PREFIX = "[SS] ";
        Component BUILD_ALREADY_EXISTS = text(PREFIX + "A build with that name already exists.", RED);
        Component NO_PERMISSION = text(PREFIX + "You don't have permission to run this command.", RED);
        Component PLUGIN_RELOADED = text(PREFIX + "Submissions reloaded. Check console for errors.", GOLD);
        String PLAYER_NOT_FOUND = PREFIX + "Could not find a player with that name.";
        Component BUILD_DOES_NOT_EXIST = text(PREFIX + "A build with that name does not exist.", RED);
        Component REWARDS_RECEIVED = text(PREFIX + "All rewards have been given to you.", GOLD);
        Component REWARDS_WAITING = text(PREFIX + "Hey, you! You have rewards waiting for you. Click this message to claim them!", GOLD).clickEvent(ClickEvent.runCommand("/ss claim"));
        Component SET_BUILD_NAME = text(PREFIX + "Set the name of your build.", GREEN);

        @Nonnull
        static String failedToReadFile(@Nonnull File file) {
            return "Failed to read " + file.getName();
        }

        @Nonnull
        static String failedToWriteFile(@Nonnull File file) {
            return "Failed to write " + file.getName();
        }

        @Nonnull
        static Component locationChanged(@Nonnull Build build) {
            return text(PREFIX + "Warp location for " + build.getName() + " updated.", GREEN);
        }

        @Nonnull
        static Component rewardsGiven(String name) {
            return text(PREFIX + "Rewards given to " + name, GOLD);
        }

        @Nonnull
        static String teleportedToBuild(@Nonnull Build build) {
            return PREFIX + "You have teleported to " + build.getName();
        }
    }

    interface Permissions {

        //@formatter:off
        String BASE = "ss.";
        String ADMIN = BASE + "admin";
        String FEATURE = BASE + "feature";
        String SUBMIT = BASE + "submit";
        String VIEW = BASE + "view";
        String VIEW_GOTO = VIEW + ".goto";
        //@formatter:on
    }
}
