package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.SpongeServerSaturday;
import com.campmongoose.serversaturday.sponge.command.args.BuildCommandElement;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterBuildCommandElement;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterCommandElement;
import com.campmongoose.serversaturday.sponge.command.feature.SSFeature;
import com.campmongoose.serversaturday.sponge.command.feature.SSGiveReward;
import com.campmongoose.serversaturday.sponge.command.submit.SSDescription;
import com.campmongoose.serversaturday.sponge.command.submit.SSEdit;
import com.campmongoose.serversaturday.sponge.command.submit.SSGetRewards;
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
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import static org.spongepowered.api.command.args.GenericArguments.optional;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.text.Text.of;

public class SpongeCommands {

    private SpongeCommands() {

    }

    public static void init(SpongeServerSaturday plugin) {
        String fullPrefix = Reference.ID.replace("_", "");
        String shortPrefix = Commands.SS_CMD.replace("/", "");
        CommandManager cm = Sponge.getCommandManager();
        cm.register(plugin, CommandSpec.builder().description(of(Commands.HELP_DESC)).executor(new SSCommand()).build(), fullPrefix, shortPrefix);
        cm.register(plugin, CommandSpec.builder().description(of(Commands.DESCRIPTION_DESC)).arguments(new BuildCommandElement()).executor(new SSDescription()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.DESCRIPTION_NAME, shortPrefix + Commands.DESCRIPTION_NAME, shortPrefix + "d");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.EDIT_DESC)).arguments(new BuildCommandElement()).executor(new SSEdit()).build(), fullPrefix + Commands.EDIT_NAME, shortPrefix + Commands.EDIT_NAME, shortPrefix + "e");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.LOCATION_DESC)).arguments(new BuildCommandElement()).executor(new SSLocation()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.LOCATION_NAME, shortPrefix + Commands.LOCATION_NAME, shortPrefix + "l");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.NEW_DESC)).arguments(optional(string(of(Commands.BUILD)))).executor(new SSNew()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.NEW_NAME, shortPrefix + Commands.NEW_NAME, shortPrefix + "n");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.REMOVE_DESC)).arguments(new BuildCommandElement()).executor(new SSRemove()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.REMOVE_NAME, shortPrefix + Commands.REMOVE_NAME, shortPrefix + "rem");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.RENAME_DESC)).arguments(new BuildCommandElement()).executor(new SSRename()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.RENAME_NAME, shortPrefix + Commands.RENAME_NAME, shortPrefix + "ren");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.RESOURCE_PACK_DESC)).arguments(new BuildCommandElement()).executor(new SSResourcePack()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.RESOURCE_PACK_NAME, shortPrefix + Commands.RESOURCE_PACK_NAME, shortPrefix + "rp");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.SUBMIT_DESC)).arguments(new BuildCommandElement()).executor(new SSSubmit()).permission(Permissions.SUBMIT).build(), fullPrefix + Commands.SUBMIT_NAME, shortPrefix + Commands.SUBMIT_NAME, shortPrefix + "s");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.GOTO_DESC)).arguments(new SubmitterBuildCommandElement()).executor(new SSGoto()).permission(Permissions.VIEW_GOTO).build(), fullPrefix + Commands.GOTO_NAME, shortPrefix + Commands.GOTO_NAME, shortPrefix + "g");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.VIEW_DESC)).arguments(new SubmitterCommandElement()).arguments(new SubmitterBuildCommandElement()).executor(new SSView()).permission(Permissions.VIEW).build(), fullPrefix + Commands.VIEW_NAME, shortPrefix + Commands.VIEW_NAME, shortPrefix + "v");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.VIEW_DESCRIPTION_DESC)).arguments(new SubmitterBuildCommandElement()).executor(new SSViewDescription()).permission(Permissions.VIEW).build(), fullPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + Commands.VIEW_DESCRIPTION_NAME, shortPrefix + "vd");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.FEATURE_DESC)).arguments(optional(new SubmitterBuildCommandElement())).executor(new SSFeature()).permission(Permissions.FEATURE).build(), fullPrefix + Commands.FEATURE_NAME, shortPrefix + Commands.FEATURE_NAME, shortPrefix + "f");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.GIVE_REWARD_DESC)).arguments(GenericArguments.user(Text.of(Commands.PLAYER))).executor(new SSGiveReward()).permission(Permissions.FEATURE).build(), fullPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + Commands.GIVE_REWARD_NAME, shortPrefix + "gr");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.GET_REWARDS_DESC)).executor(new SSGetRewards()).permission(Permissions.FEATURE).build(), fullPrefix + Commands.GET_REWARDS_NAME, shortPrefix + Commands.GET_REWARDS_NAME, shortPrefix + "getr");
        cm.register(plugin, CommandSpec.builder().description(of(Commands.RELOAD_DESC)).executor(new SSReload()).permission(Permissions.RELOAD).build(), fullPrefix + Commands.RELOAD_NAME, shortPrefix + Commands.RELOAD_NAME, shortPrefix + "r");
    }
}
