package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.submission.Build;
import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

abstract class SSBuild extends ServerSaturdayCommand implements ArgumentCommand<Map<UUID, Build>> {

    @NotNull
    @Override
    public String name() {
        return BUILD;
    }

    @NotNull
    @Override
    public ArgumentType<Map<UUID, Build>> type() {
        return new BuildArgumentType();
    }
}
