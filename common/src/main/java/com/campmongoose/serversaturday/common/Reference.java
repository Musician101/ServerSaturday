package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Build;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.ComponentLike.asComponents;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

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

        Component BACK = text("Back", WHITE);
        Component CHANGE_DESCRIPTION_DESC = text("Add or change the description to this build.");
        Component CHANGE_DESCRIPTION_NAME = text("Change Description");
        List<Component> CHANGE_LOCATION_DESC = toComponents("Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build.");
        Component CHANGE_LOCATION_NAME = text("Change Location");
        List<Component> CHANGE_RESOURCES_PACK_DESC = toComponents("Change the recommended resource", "packs for this build.");
        Component CHANGE_RESOURCE_PACKS_NAME = text("Change Resource Packs");
        Component DESCRIPTION_DESC = text("View this build's description.");
        Component DESCRIPTION_NAME = text("Description");
        List<Component> FEATURE_DESC = toComponents("Set whether this build has been covered in", "an episode of Server Saturday.");
        Component FEATURE_NAME = text("Feature");
        Component RENAME_DESC = text("Rename this build.");
        Component RENAME_NAME = text("Rename");
        Component RESOURCE_PACK_DESC = text("View this build's recommended resource packs.");
        Component RESOURCE_PACK_NAME = text("Resource Pack");
        List<Component> SUBMIT_UNREADY_DESC = toComponents("Add or remove your build from", "the list of ready builds.");
        Component SUBMIT_UNREADY_NAME = text("Submit/Unready");
        Component TELEPORT_NAME = text("Teleport");

        @NotNull
        static List<Component> teleportDesc(@NotNull String name, int x, int y, int z) {
            return toComponents("Click to teleport.", "- World: " + name, "- X: " + x, "- Y: " + y, "- Z: " + z);
        }

        private static List<Component> toComponents(String... s) {
            return asComponents(Stream.of(s).map(Component::text).toList());
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
        String FAILED_TO_READ_SUBMITTERS = "Failed to read submitters directory!";
        Component PLAYER_ONLY_COMMAND = text(PREFIX + "This is a player only command.", RED);

        @NotNull
        static String failedToReadFile(@NotNull Path file) {
            return "Failed to read " + file.getFileName().toString();
        }

        @NotNull
        static String failedToWriteFile(@NotNull Path path) {
            return "Failed to write " + path.getFileName();
        }

        @NotNull
        static Component locationChanged(@NotNull Build build) {
            return text(PREFIX + "Warp location for " + build.getName() + " updated.", GREEN);
        }

        @NotNull
        static Component rewardsGiven(String name) {
            return text(PREFIX + "Rewards given to " + name, GOLD);
        }

        @NotNull
        static String teleportedToBuild(@NotNull Build build) {
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
