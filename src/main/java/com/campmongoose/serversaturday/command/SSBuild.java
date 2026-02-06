package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.musicommand.paper.command.PaperArgumentCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class SSBuild implements PaperArgumentCommand.AdventureFormat<Holder>, SSCommand {

    @Override
    public String name() {
        return BUILD;
    }

    @Override
    public ArgumentType<Holder> type() {
        return BuildArgumentType.SUBMITTER;
    }
}
