package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.Build;
import com.campmongoose.serversaturday.common.submission.Submitter;
import io.musician101.musicianlibrary.java.minecraft.common.Location;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nonnull;

public interface Reference {

    String DESCRIPTION = "Plugin based submission form for Potato's Server Saturday.";
    String ID = "serversaturday";
    String NAME = "Server Saturday";
    String VERSION = "3.2.2";

    interface Commands {

        String BUILD = "build";
        String DESCRIPTION_DESC = "Change the description of a submission.";
        String DESCRIPTION_NAME = "description";
        String EDIT_DESC = "Edit a submitted build.";
        String EDIT_NAME = "edit";
        String FEATURE_DESC = "Toggle if a build has been featured.";
        String FEATURE_NAME = "feature";
        String GET_REWARDS_DESC = "Receive any pending rewards.";
        String GET_REWARDS_NAME = "getrewards";
        String GIVE_REWARD_DESC = "Give a player a reward.";
        String GIVE_REWARD_NAME = "givereward";
        String GOTO_DESC = "Teleport to a build.";
        String GOTO_NAME = "goto";
        String HELP_DESC = "Displays help and plugin info.";
        String LOCATION_DESC = "Change the location of a build.";
        String LOCATION_NAME = "location";
        String NAME = "name";
        String NEW_DESC = "Register a new build.";
        String NEW_NAME = "new";
        String PLAYER = "player";
        String RELOAD_DESC = "Reload the plugin.";
        String RELOAD_NAME = "reload";
        String REMOVE_DESC = "Remove a build.";
        String REMOVE_NAME = "remove";
        String RENAME_DESC = "Rename a submission.";
        String RENAME_NAME = "rename";
        String RESOURCE_PACK_DESC = "Change the recommended resource pack.";
        String RESOURCE_PACK_NAME = "resourcepack";
        String SS_CMD = "/ss";
        String SUBMIT_DESC = "Toggle whether you build is ready to be featured or not.";
        String SUBMIT_NAME = "submit";
        String VIEW_ALL_DESC = "View all builds that have been submitted.";
        String VIEW_ALL_NAME = "viewall";
        String VIEW_DESC = "View Server Saturday submissions.";
        String VIEW_DESCRIPTION_DESC = "View the description of a build.";
        String VIEW_DESCRIPTION_NAME = "viewdescription";
        String VIEW_NAME = "view";
        String VIEW_RESOURCE_PACK_DESC = "View the build's recommended resource pack.";
        String VIEW_RESOURCE_PACK_NAME = "viewresourcepack";
    }

    interface Config {

        String ADDRESS = "address";
        String BUILDS = "builds";
        String DATABASE = "database";
        String DESCRIPTION = "description";
        String FEATURED = "featured";
        String HOCON = "HOCON";
        String JSON = "JSON";
        String LOCATION = "location";
        String MAX_BUILDS = "max_builds";
        String MONGO_DB = "MongoDB";
        String MONGO_URI = "mongo_uri";
        String MYSQL = "MYSQL";
        String NAME = "name";
        String PASSWORD = "password";
        String RESOURCE_PACK = "resource_pack";
        String REWARDS = "rewards";
        String SQLITE = "SQLite";
        String SUBMITTED = "submitted";
        String USER = "user";
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
        String USERNAME = "Username";
        String UUID = "UUID";
        String WORLD_NAME = "WorldName";
        String X = "X";
        String Y = "Y";
        String Z = "Z";
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + UUID + " TEXT, " + USERNAME + " TEXT, " + BUILD_NAME + " TEXT, " + FEATURED + " BOOLEAN, " + SUBMITTED + " BOOLEAN, " + WORLD_NAME + " TEXT, " + X + " DECIMAL, " + Y + " DECIMAL, " + Z + " DECIMAL, " + DESCRIPTION + " TEXT, " + RESOURCE_PACKS + " TEXT";
        String SELECT_TABLE = "SELECT * FROM " + TABLE_NAME;
        String CLEAR_TABLE = "DELETE FROM " + TABLE_NAME;

        static <T> String addBuild(@Nonnull Submitter<T> submitter, @Nonnull Build<T> build, @Nonnull Function<List<T>, String> textToString) {
            Location location = build.getLocation();
            return "INSERT FROM " + TABLE_NAME + "(" + UUID + ", " + USERNAME + ", " + BUILD_NAME + ", " + FEATURED + ", " + SUBMITTED + ", " + WORLD_NAME + ", " + X + ", " + Y + ", " + Z + ", " + DESCRIPTION + ", " + RESOURCE_PACKS + ") VALUES (\""
                    + submitter.getUUID() + "\", \"" + submitter.getName() + "\", \"" + build.getName() + "\", \"" + location.getWorldName() + "\", \"" + location.getX() + "\", \"" + location.getY() + "\", \"" + location.getZ() + "\", \""
                    + textToString.apply(build.getDescription()) + "\", \"" + textToString.apply(build.getResourcePacks()) + ")";
        }
    }

    interface MenuText {

        String ALL_SUBMISSIONS = "All S.S. Submissions";
        String ALREADY_EXISTS = "A build with that name already exists!";
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
        String REWARDS = "S. S. Rewards";
        String SUBMISSIONS = "S. S. Submissions";
        List<String> SUBMIT_UNREADY_DESC = Arrays.asList("Add or remove your build from", "the list of ready builds.");
        String SUBMIT_UNREADY_NAME = "Submit/Unready";
        String TELEPORT_NAME = "Teleport";

        @Nonnull
        static String submitterMenu(@Nonnull Submitter<?> submitter) {
            return submitter.getName() + "'s Builds";
        }

        @Nonnull
        static List<String> teleportDesc(@Nonnull String name, int x, int y, int z) {
            return Arrays.asList("Click to teleport.", "- World: " + name, "- X: " + x, "- Y: " + y, "- Z: " + z);
        }
    }

    interface Messages {

        String CONFIG_LOADED = "Config loaded.";
        String LOADING_CONFIG = "Loading config...";
        String LOADING_SUBMISSIONS = "Loading submissions...";
        String PREFIX = "[SS] ";
        String BUILD_ALREADY_EXISTS = PREFIX + "A build with that name already exists.";
        String BUILD_NOT_FOUND = PREFIX + "A build with that name does not exist.";
        String EDIT_IN_PROGRESS = PREFIX + "You're in the middle of editing another build.";
        String NOT_ENOUGH_ARGS = PREFIX + "Not enough arguments.";
        String NO_PERMISSION = PREFIX + "You don't have permission to run this command.";
        String PLUGIN_RELOADED = PREFIX + "Submissions reloaded. Check console for errors.";
        String PLAYER_NOT_FOUND = PREFIX + "Could not find a player with that name.";
        String PLAYER_ONLY = PREFIX + "This is a player only command.";
        String HAND_NOT_EMPTY = PREFIX + "You need an empty in order to run this command.";
        String REWARDS_RECEIVED = PREFIX + "All rewards have been given to you.";
        String REWARDS_WAITING = PREFIX + "Hey, you! You have rewards waiting for you. Claim them with /ssgetrewards";
        String SAVING_SUBMISSIONS = "Saving submissions to disk...";
        String SET_BUILD_NAME = PREFIX + "Set the name of your build.";
        String SQL_EXCEPTION = "An error occurred while executing queries.";
        String SUBMISSIONS_LOADED = "Submissions loaded.";
        String SUBMISSIONS_SAVED = "Save complete.";
        String CONFIG_READ_ERROR = "Failed to read the config.";

        @Nonnull
        static String failedToReadFile(@Nonnull File file) {
            return "Failed to read " + file.getName();
        }

        @Nonnull
        static String failedToReadFiles(@Nonnull File dir) {
            return "An error occurred whilst attempting to read the files in " + dir.getName();
        }

        @Nonnull
        static String failedToSaveRewardsFile(@Nonnull File file) {
            return PREFIX + failedToWriteFile(file) + " Returning contents just in case.";
        }

        @Nonnull
        static String failedToWriteFile(@Nonnull File file) {
            return "Failed to write " + file.getName();
        }

        @Nonnull
        static String locationChanged(@Nonnull Build<?> build) {
            return PREFIX + "Warp location for " + build.getName() + " updated.";
        }

        @Nonnull
        static String rewardsGiven(String name) {
            return PREFIX + "Rewards given to " + name;
        }

        @Nonnull
        static String teleportedToBuild(@Nonnull Build<?> build) {
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
