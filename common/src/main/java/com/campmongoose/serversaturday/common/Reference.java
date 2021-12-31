package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;

public interface Reference {

    String ID = "serversaturday";
    String NAME = "Server Saturday";
    String VERSION = "3.3";

    interface Commands {

        String EDIT_DESC = "Edit a submitted build.";
        String EDIT_NAME = "edit";
        String GET_REWARDS_DESC = "Receive any pending rewards.";
        String GET_REWARDS_NAME = "getrewards";
        String GIVE_REWARD_DESC = "Give a player a reward.";
        String GIVE_REWARD_NAME = "givereward";
        String HELP_DESC = "Displays help and plugin info.";
        String PLAYER = "player";
        String RELOAD_DESC = "Reload the plugin.";
        String RELOAD_NAME = "reload";
        String SS_CMD = "ss";
        String VIEW_ALL_DESC = "View all builds that have been submitted.";
        String VIEW_ALL_NAME = "viewall";
        String VIEW_DESC = "View Server Saturday submissions.";
        String VIEW_NAME = "view";
        String BUILD = "build";
    }

    interface Config {

        String BUILDS = "builds";
        String DATABASE = "database";
        String DESCRIPTION = "description";
        String FEATURED = "featured";
        String HOCON = "HOCON";
        String JSON = "JSON";
        String LOCATION = "location";
        String MAX_BUILDS = "max_builds";
        String MONGO_DB = "MongoDB";
        String MYSQL = "MYSQL";
        String NAME = "name";
        String RESOURCE_PACK = "resource_pack";
        String REWARDS = "rewards";
        String SQLITE = "SQLite";
        String SUBMITTED = "submitted";
        String UUID = "uuid";
        String YAML = "YAML";

        @Nonnull
        static String getFileName(@Nonnull UUID uuid) {
            return uuid + JSON;
        }
    }

    interface Database {

        String BUILD_NAME = "BuildName";
        String DESCRIPTION = "Description";
        String FEATURED = "Featured";
        String RESOURCE_PACKS = "ResourcePacks";
        String SUBMITTED = "Submitted";
        String TABLE_NAME = "ss_builds";
        String SELECT_TABLE = "SELECT * FROM " + TABLE_NAME;
        String CLEAR_TABLE = "DELETE FROM " + TABLE_NAME;
        String USERNAME = "Username";
        String UUID = "UUID";
        String WORLD_NAME = "WorldName";
        String X = "X";
        String Y = "Y";
        String Z = "Z";
        String PITCH = "Pitch";
        String YAW = "Yaw";
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + UUID + " TEXT, " + USERNAME + " TEXT, " + BUILD_NAME + " TEXT, " + FEATURED + " BOOLEAN, " + SUBMITTED + " BOOLEAN, " + WORLD_NAME + " TEXT, " + X + " DECIMAL, " + Y + " DECIMAL, " + Z + " DECIMAL, " + PITCH + " FLOAT, " + YAW + " FLOAT, " + DESCRIPTION + " TEXT, " + RESOURCE_PACKS + " TEXT";

        static String addBuild(@Nonnull Submitter submitter, @Nonnull Build build) {
            Location location = build.getLocation();
            return "INSERT FROM " + TABLE_NAME + "(" + UUID + ", " + USERNAME + ", " + BUILD_NAME + ", " + FEATURED + ", " + SUBMITTED + ", " + WORLD_NAME + ", " + X + ", " + Y + ", " + Z + ", " + DESCRIPTION + ", " + RESOURCE_PACKS + ") VALUES (\"" + submitter.getUUID() + "\", \"" + submitter.getName() + "\", \"" + build.getName() + "\", \"" + location.getWorldName() + "\", \"" + location.getX() + "\", \"" + location.getY() + "\", \"" + location.getZ() + "\", \"" + build.getDescription() + "\", \"" + build.getResourcePack() + ")";
        }
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

        String CONFIG_LOADED = "Config loaded.";
        String CONFIG_READ_ERROR = "Failed to read the config.";
        String LOADING_CONFIG = "Loading config...";
        String LOADING_SUBMISSIONS = "Loading submissions...";
        String PREFIX = "[SS] ";
        String BUILD_ALREADY_EXISTS = PREFIX + "A build with that name already exists.";
        String EDIT_IN_PROGRESS = PREFIX + "You're in the middle of editing another build.";
        String NO_PERMISSION = PREFIX + "You don't have permission to run this command.";
        String PLUGIN_RELOADED = PREFIX + "Submissions reloaded. Check console for errors.";
        String PLAYER_NOT_FOUND = PREFIX + "Could not find a player with that name.";
        String PLAYER_ONLY = PREFIX + "This is a player only command.";
        String HAND_NOT_EMPTY = PREFIX + "You need an empty in order to run this command.";
        String REWARDS_RECEIVED = PREFIX + "All rewards have been given to you.";
        String REWARDS_WAITING = PREFIX + "Hey, you! You have rewards waiting for you. Claim them with /ssgetrewards";
        String SAVING_SUBMISSIONS = "Saving submissions to disk...";
        String SET_BUILD_NAME = PREFIX + "Set the name of your build.";
        String SUBMISSIONS_LOADED = "Submissions loaded.";
        String SUBMISSIONS_SAVED = "Save complete.";
        String BUILD_DOES_NOT_EXIST = PREFIX + "A build with that name does not exist.";

        @Nonnull
        static String failedToReadFile(@Nonnull File file) {
            return "Failed to read " + file.getName();
        }

        @Nonnull
        static String failedToReadFiles(@Nonnull File dir) {
            return "An error occurred whilst attempting to read the files in " + dir.getName();
        }

        @Nonnull
        static String failedToWriteFile(@Nonnull File file) {
            return "Failed to write " + file.getName();
        }

        @Nonnull
        static String locationChanged(@Nonnull Build build) {
            return PREFIX + "Warp location for " + build.getName() + " updated.";
        }

        @Nonnull
        static String rewardsGiven(String name) {
            return PREFIX + "Rewards given to " + name;
        }

        @Nonnull
        static String teleportedToBuild(@Nonnull Build build) {
            return PREFIX + "You have teleported to " + build.getName();
        }
    }

    interface Permissions {

        //@formatter:off
        String BASE = "ss.";
        String FEATURE = BASE + "feature";
        String RELOAD = BASE + "reload";
        String SUBMIT = BASE + "submit";
        String EXCEED_MAX_BUILDS = SUBMIT + "exceed_max_builds";
        String VIEW = BASE + "view";
        String VIEW_GOTO = VIEW + ".goto";
        //@formatter:on
    }
}
