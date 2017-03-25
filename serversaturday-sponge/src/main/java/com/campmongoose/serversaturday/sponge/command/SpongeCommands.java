package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.command.submit.SSDescription;
import com.campmongoose.serversaturday.sponge.command.submit.SSEdit;
import com.campmongoose.serversaturday.sponge.command.submit.SSLocation;
import com.campmongoose.serversaturday.sponge.command.submit.SSNew;
import com.campmongoose.serversaturday.sponge.command.submit.SSRemove;
import com.campmongoose.serversaturday.sponge.command.submit.SSRename;
import com.campmongoose.serversaturday.sponge.command.submit.SSResourcePack;
import com.campmongoose.serversaturday.sponge.command.submit.SSSubmit;
import com.campmongoose.serversaturday.sponge.command.view.SSGoto;
import com.campmongoose.serversaturday.sponge.command.view.SSView;
import com.campmongoose.serversaturday.sponge.command.view.SSViewDescription;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;

import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.remainingJoinedStrings;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.text.Text.of;

public class SpongeCommands {

    private SpongeCommands() {

    }

    private static SpongeServerSaturday getPluginInstance() {
        return SpongeServerSaturday.instance();
    }

    public static void init() {
        String fullPrefix = Reference.ID.replace("_", "");
        String shortPrefix = Commands.SS_CMD.replace("/", "");
        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().extendedDescription(of(Commands.HELP_DESC))
                .executor(new SSCommand()).build(), fullPrefix, shortPrefix);

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.DESCRIPTION_DESC))
                        .arguments(remainingJoinedStrings(of(Commands.BUILD))).executor(new SSDescription()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.DESCRIPTION_NAME, shortPrefix + Commands.DESCRIPTION_NAME, shortPrefix + "d");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().extendedDescription(of(Commands.EDIT_DESC))
                        .arguments(optional(remainingJoinedStrings(of(Commands.BUILD)))).executor(new SSEdit()).build(),
                fullPrefix + Commands.EDIT_NAME, shortPrefix + Commands.EDIT_NAME, shortPrefix + "e");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.LOCATION_DESC))
                        .arguments(remainingJoinedStrings(of(Commands.BUILD))).executor(new SSLocation()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.LOCATION_NAME, shortPrefix + Commands.LOCATION_NAME, shortPrefix + "l");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.NEW_DESC))
                        .arguments(optional(string(of(Commands.NAME)))).executor(new SSNew()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.NEW_NAME, shortPrefix + Commands.NEW_NAME, shortPrefix + "n");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.REMOVE_DESC))
                        .arguments(optional(remainingJoinedStrings(of(Commands.BUILD)))).executor(new SSRemove()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.REMOVE_NAME, shortPrefix + Commands.REMOVE_NAME, shortPrefix + "rem");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.RENAME_DESC))
                        .arguments(remainingJoinedStrings(of(Commands.BUILD))).executor(new SSRename()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.RENAME_NAME, shortPrefix + Commands.RENAME_NAME, shortPrefix + "ren");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.RESOURCE_PACK_DESC))
                        .arguments(remainingJoinedStrings(of(Commands.BUILD))).executor(new SSResourcePack()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.RESOURCE_PACK_NAME, shortPrefix + Commands.RESOURCE_PACK_NAME, shortPrefix + "rp");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.SUBMIT_DESC))
                        .arguments(remainingJoinedStrings(of(Commands.BUILD))).executor(new SSSubmit()).permission(Permissions.SUBMIT).build(),
                fullPrefix + Commands.SUBMIT_NAME, shortPrefix + Commands.SUBMIT_NAME, shortPrefix + "s");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.GOTO_DESC))
                        .arguments(string(of(Commands.PLAYER)), remainingJoinedStrings(of(Commands.BUILD))).executor(new SSGoto()).permission(Permissions.VIEW_GOTO).build(),
                fullPrefix + Commands.GOTO_NAME, shortPrefix + Commands.GOTO_NAME, shortPrefix + "g");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.VIEW_DESC))
                        .arguments(optional(string(of(Commands.PLAYER))), optional(remainingJoinedStrings(of(Commands.BUILD)))).executor(new SSView()).permission(Permissions.VIEW).build(),
                fullPrefix + Commands.VIEW_NAME, shortPrefix + Commands.VIEW_NAME, shortPrefix + "v");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.VIEW_DESCRIPTION_DESC))
                        .arguments(string(of(Commands.PLAYER)), remainingJoinedStrings(of(Commands.BUILD))).executor(new SSViewDescription()).permission(Permissions.VIEW).build(),
                fullPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + "vd");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.FEATURE_DESC))
                        .arguments(optional(string(of(Commands.PLAYER))), optional(remainingJoinedStrings(of(Commands.BUILD)))).executor(new SSFeature()).permission(Permissions.FEATURE).build(),
                fullPrefix + Commands.FEATURE_NAME, shortPrefix + Commands.FEATURE_NAME, shortPrefix + "f");

        Sponge.getCommandManager().register(getPluginInstance(), CommandSpec.builder().description(of(Commands.RELOAD_DESC))
                        .executor(new SSReload()).permission(Permissions.RELOAD).build(),
                fullPrefix + Commands.RELOAD_NAME, shortPrefix + Commands.RELOAD_NAME, shortPrefix + "r");
    }
}
