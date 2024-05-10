package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType.Holder;
import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import org.jetbrains.annotations.NotNull;

abstract class SSBuild extends ServerSaturdayCommand implements ArgumentCommand<Holder> {

    @NotNull
    @Override
    public String name() {
        return BUILD;
    }

    @NotNull
    @Override
    public ArgumentType<Holder> type() {
        return BuildArgumentType.SUBMITTER;
    }
}
