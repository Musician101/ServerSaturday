package com.campmongoose.serversaturday.forge.command.sscommand.feature;

import com.campmongoose.serversaturday.common.Reference.Commands;
import com.campmongoose.serversaturday.forge.command.ForgeCommand;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SSFeature extends ForgeCommand {

    public SSFeature() {
        super(Commands.FEATURE_DESC);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ForgeSubmitter submitter = context.getArgument("submitter", ForgeSubmitter.class);
        if (submitter != null) {

            ForgeBuild build = submitter.getBuild(context.getArgument(Commands.BUILD, String.class));
            if (build != null) {
                build.setFeatured(!build.featured());
                if (submitter.getUUID().equals(player.getUniqueID())) {
                    //TODO open GUI
                    //new EditBuildGUI(build, submitter, player);
                }
                else {
                    //TODO open GUI
                    //new ViewBuildGUI(build, submitter, player);
                }

                return SINGLE_SUCCESS;
            }

            //TODO open gui
            //new SubmitterGUI(submitter, player);
            return SINGLE_SUCCESS;
        }

        //TODO open gui
        //new AllSubmissionsGUI(player);
        return SINGLE_SUCCESS;
    }
}
