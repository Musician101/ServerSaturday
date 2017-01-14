package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.common.Reference.Permissions;
import com.campmongoose.serversaturday.sponge.command.SSFeature;
import com.campmongoose.serversaturday.sponge.command.SSReload;
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
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmissions;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(id = Reference.ID, name = Reference.NAME, description = Reference.DESCRIPTION, version = Reference.VERSION)
public class SpongeServerSaturday {

    private SpongeDescriptionChangeHandler dch;
    private SpongeSubmissions submissions;

    public static Logger getLogger() {
        return getPluginContainer().getLogger();
    }

    public static PluginContainer getPluginContainer() {
        return Sponge.getPluginManager().getPlugin(Reference.ID).get();
    }

    public static SpongeServerSaturday instance() {
        return (SpongeServerSaturday) getPluginContainer().getInstance().get();
    }

    public SpongeDescriptionChangeHandler getDescriptionChangeHandler() {
        return dch;
    }

    public SpongeSubmissions getSubmissions() {
        return submissions;
    }

    @Listener
    public void onDisable(GamePostInitializationEvent event) {
        submissions.save();
    }

    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        dch = new SpongeDescriptionChangeHandler();
        submissions = new SpongeSubmissions();
        Sponge.getCommandManager().register(this, CommandSpec.builder().description(Text.of(Reference.DESCRIPTION))
                .child(CommandSpec.builder().description(Text.of(Commands.DESCRIPTION_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSDescription())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.DESCRIPTION_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.EDIT_DESC))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.BUILD))))
                        .executor(new SSEdit())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.EDIT_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.LOCATION_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSLocation())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.LOCATION_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.NEW_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.NAME)))
                        .executor(new SSNew())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.NEW_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.REMOVE_DESC))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.BUILD))))
                        .executor(new SSRemove())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.REMOVE_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.RENAME_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSRename())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.RENAME_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.RESOURCE_PACK_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSResourcePack())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.RESOURCE_PACK_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.SUBMIT_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSSubmit())
                        .permission(Permissions.SUBMIT)
                        .build(), Commands.SUBMIT_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.GOTO_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.PLAYER)))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSGoto())
                        .permission(Permissions.VIEW_GOTO)
                        .build(), Commands.GOTO_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.VIEW_DESC))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.PLAYER))))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.BUILD))))
                        .executor(new SSView())
                        .permission(Permissions.VIEW)
                        .build(), Commands.VIEW_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.VIEW_DESCRIPTION_DESC))
                        .arguments(GenericArguments.string(Text.of(Commands.PLAYER)))
                        .arguments(GenericArguments.string(Text.of(Commands.BUILD)))
                        .executor(new SSViewDescription())
                        .permission(Permissions.VIEW)
                        .build(), Commands.VIEW_DESCRIPTION_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.FEATURE_DESC))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.PLAYER))))
                        .arguments(GenericArguments.optional(GenericArguments.string(Text.of(Commands.BUILD))))
                        .executor(new SSFeature())
                        .permission(Permissions.FEATURE)
                        .build(), Commands.FEATURE_NAME)
                .child(CommandSpec.builder().description(Text.of(Commands.RELOAD_DESC))
                        .executor(new SSReload())
                        .permission(Permissions.RELOAD)
                        .build(), Commands.RELOAD_NAME)
                .build(), Reference.NAME.replace(" ", ""), Commands.SS_CMD.replace("/", ""));
    }
}
