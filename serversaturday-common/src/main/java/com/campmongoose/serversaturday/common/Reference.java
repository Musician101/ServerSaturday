package com.campmongoose.serversaturday.common;

import com.campmongoose.serversaturday.common.submission.AbstractBuild;
import com.campmongoose.serversaturday.common.submission.AbstractSubmitter;
import com.google.gson.Gson;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;

public class Reference {

    public static final String DESCRIPTION = "Plugin based submission form for Potato's Server Saturday.";
    public static final String DUCK = "\\_o<";
    public static final String ID = "server_saturday";
    public static final String NAME = "Server Saturday";
    public static final String VERSION = "2.0";

    private Reference() {

    }

    public static class Commands {

        public static final String BUILD = "build";
        public static final String DESCRIPTION_DESC = "Change the description of a submission.";
        public static final String DESCRIPTION_NAME = "description";
        public static final String EDIT_DESC = "Edit a submitted build.";
        public static final String EDIT_NAME = "edit";
        public static final String FEATURE_DESC = "Toggle if a build has been featured on Server Saturday.";
        public static final String FEATURE_NAME = "feature";
        public static final String GOTO_DESC = "Teleport to a build.";
        public static final String GOTO_NAME = "goto";
        public static final String HELP_DESC_PREFIX = "Display help info for ";
        public static final String HELP_NAME = "help";
        public static final String LOCATION_DESC = "Change the location of a build.";
        public static final String LOCATION_NAME = "location";
        public static final String NAME = "name";
        public static final String NEW_DESC = "Register a new build.";
        public static final String NEW_NAME = "new";
        public static final String PLAYER = "player";
        public static final String RELOAD_DESC = "Reload the plugin";
        public static final String RELOAD_NAME = "reload";
        public static final String REMOVE_DESC = "Remove a build.";
        public static final String REMOVE_NAME = "remove";
        public static final String RENAME_DESC = "Rename a submission.";
        public static final String RENAME_NAME = "rename";
        public static final String RESOURCE_PACK_DESC = "Change the recommended resource pack.";
        public static final String RESOURCE_PACK_NAME = "resourcepack";
        public static final String SS_CMD = "/ss";
        public static final String SUBMIT_DESC = "Toggle whether you build is ready to be featured or not.";
        public static final String SUBMIT_NAME = "submit";
        public static final String VIEW_DESC = "View Server Saturday submissions.";
        public static final String VIEW_DESCRIPTION_DESC = "View the description of a build.";
        public static final String VIEW_DESCRIPTION_NAME = "viewdescription";
        public static final String VIEW_NAME = "view";

        private Commands() {

        }
    }

    public static class Config {

        public static final String BUILDS = "builds";
        public static final String DATABASE = "database";
        public static final String DESCRIPTION = "description";
        public static final String FEATURED = "featured";
        public static final String HOCON_EXT = ".conf";
        public static final String HOST = "host";
        public static final String LOCATION = "location";
        public static final String MYSQL = "mysql";
        public static final String NAME = "name";
        public static final String PASS = "password";
        public static final String PORT = "port";
        public static final String RESOURCE_PACK = "resource_pack";
        public static final String SUBMITTED = "submitted";
        public static final String USER = "user";
        public static final String VANILLA = "Vanilla";
        public static final String YAML_EXT = ".yml";

        private Config() {

        }

        @Nonnull
        public static String getHOCONFileName(@Nonnull UUID uuid) {
            return uuid.toString() + HOCON_EXT;
        }

        @Nonnull
        public static String getYAMLFileName(@Nonnull UUID uuid) {
            return uuid.toString() + YAML_EXT;
        }
    }

    public static class MenuText {

        public static final String ALL_SUBMISSIONS = "All S.S. Submissions";
        public static final String BACK = "Back";
        public static final String CHANGE_DESCRIPTION_DESC = "Add or change the description to this build.";
        public static final String CHANGE_DESCRIPTION_NAME = "Change Description";
        public static final List<String> CHANGE_LOCATION_DESC = Collections.unmodifiableList(Arrays.asList("Change the warp location for this build", "to where you are currently standing.", "WARNING: This will affect which direction", "people face when they teleport to your build."));
        public static final String CHANGE_LOCATION_NAME = "Change Location";
        public static final List<String> CHANGE_RESOURCE_PACK_DESC = Collections.unmodifiableList(Arrays.asList("Change the recommended resource", "pack for this build."));
        public static final String CHANGE_RESOURCE_PACK_NAME = "Change Resource Pack";
        public static final List<String> DELETE_DESC = Collections.unmodifiableList(Arrays.asList("THIS WILL DELETE THIS BUILD", "FROM THE SUBMISSION LIST!!!"));
        public static final String DELETE_NAME = "Delete";
        public static final String DESCRIPTION_NAME = "Description";
        public static final List<String> FEATURE_DESC = Collections.unmodifiableList(Arrays.asList("Set whether this build has been covered in", "an episode of Server Saturday."));
        public static final String FEATURE_NAME = "Feature";
        public static final String JUMP_PAGE = "Jump To Page";
        public static final String NOT_A_NUMBER = "That was not a number.";
        public static final String NEXT_PAGE = "Next Page";
        public static final String PREVIOUS_PAGE = "Previous Page";
        public static final String RENAME_DESC = "Rename this build.";
        public static final String RENAME_ME = "Rename me!";
        public static final String RENAME_NAME = "Rename";
        public static final String RESOURCE_PACK_NAME = "Resource Pack";
        public static final String SUBMISSIONS = "S. S. Submissions";
        public static final List<String> SUBMIT_UNREADY_DESC = Collections.unmodifiableList(Arrays.asList("Add or remove your build from", "the list of ready builds."));
        public static final String SUBMIT_UNREADY_NAME = "Submit/Unready";
        public static final String TELEPORT_NAME = "Teleport";
        public static final String[] BACK_DESC = {"Closes this menu and attempts","to go back to the previous one."};
        public static final String ALREADY_EXISTS = "That field already exists!";
        public static final String BUILD_DEFAULT_NAME = "A Server Saturday Build";

        private MenuText() {

        }

        @Nonnull
        public static <S extends AbstractSubmitter> String submitterMenu(@Nonnull S submitter) {
            return submitter.getName() + "'s Builds";
        }

        @Nonnull
        public static List<String> teleportDesc(@Nonnull String name, int x, int y, int z) {
            return Arrays.asList("Click to teleport.", "- World: " + name, "- X: " + x, "- Y: " + y, "- Z: " + z);
        }
    }

    public static class Messages {

        public static final String PREFIX = "[SS] ";
        public static final String BUILD_ALREADY_EXISTS = PREFIX + "A build with that name already exists.";
        public static final String BUILD_NOT_FOUND = PREFIX + "A build with that name does not exist.";
        public static final String EDIT_IN_PROGRESS = PREFIX + "You're in the middle of editing another build.";
        public static final String NOT_ENOUGH_ARGS = PREFIX + "Not enough arguments.";
        public static final String NO_PERMISSION = PREFIX + "You don't have permission to run this command.";
        public static final String PLUGIN_RELOADED = PREFIX + "Submissions reloaded. Check console for errors.";
        public static final String PLAYER_NOT_FOUND = PREFIX + "Could not find a player with that name.";
        public static final String PLAYER_ONLY = PREFIX + "This is a player only command.";

        private Messages() {

        }

        @Nonnull
        public static String ioException(@Nonnull File file) {
            return "An error occurred while saving " + file.getName();
        }

        @Nonnull
        public static <B extends AbstractBuild> String locationChanged(@Nonnull B build) {
            return PREFIX + "Warp location for " + build.getName() + " updated.";
        }

        @Nonnull
        public static String newFile(@Nonnull File file) {
            return PREFIX + "Generating a new file: " + file.getName();
        }

        @Nonnull
        public static <B extends AbstractBuild> String teleportedToBuild(@Nonnull B build) {
            return PREFIX + "You have teleported to " + build.getName();
        }
    }

    public static class MySQL {

        private static final String TABLE_NAME = "server_saturday_builds";
        public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(PlayerName text, " +
                "PlayerID text, BuildName text, Featured boolean, Submitted boolean, LocationX float, LocationY float, " +
                "LocationZ float, LocationYaw decimal, LocationPitch decimal, WorldName text, ResourcePack text, Description json);";

        private MySQL() {

        }

        @Nonnull
        public static <B extends AbstractBuild<I, L, S>, I, L, S extends AbstractSubmitter<B, I, L>> String addBuild(@Nonnull String name, UUID uuid, @Nonnull B build, double x, double y, double z, double yaw, double pitch, @Nonnull String worldName) {
            return "insert into " + TABLE_NAME + "(PlayerName, PlayerID, BuildName, Featured, Submitted, LocationX, " +
                    "LocationY, LocationZ, LocationYaw, LocationPitch, WorldName, ResourcePack, Description) values " +
                    "(\"" + name + "\", \"" + uuid.toString() + "\", \"" + build.getName() + "\", " + build.featured() +
                    ", " + build.submitted() + ", " + x + ", " + y + ", " + z + ", " + yaw + ", " + pitch + ", \"" +
                    worldName + "\", \"" + build.getResourcePack() + "\", '" + new Gson().toJson(build.getDescription()) + "');";
        }

        @Nonnull
        public static String deletePlayer(@Nonnull UUID uuid) {
            return "delete from " + TABLE_NAME + " where PlayerID = " + uuid.toString();
        }
    }

    public static class Permissions {

        private static final String BASE = "ss.";
        public static final String FEATURE = BASE + "feature";
        public static final String RELOAD = BASE + "reload";
        public static final String SUBMIT = BASE + "submit";
        public static final String VIEW = BASE + "view";
        public static final String VIEW_GOTO = VIEW + ".goto";

        private Permissions() {

        }
    }
}
