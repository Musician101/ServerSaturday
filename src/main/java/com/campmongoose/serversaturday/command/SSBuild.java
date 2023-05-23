package com.campmongoose.serversaturday.command;

import com.campmongoose.serversaturday.Reference.Commands;
import com.campmongoose.serversaturday.command.argument.BuildArgumentType;
import com.campmongoose.serversaturday.submission.Build;
import com.mojang.brigadier.arguments.ArgumentType;
import io.musician101.bukkitier.command.ArgumentCommand;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

abstract class SSBuild extends ServerSaturdayCommand implements ArgumentCommand<Map<UUID, Build>> {

    @Nonnull
    @Override
    public String name() {
        return Commands.BUILD;
    }

    @Nonnull
    @Override
    public ArgumentType<Map<UUID, Build>> type() {
        return new BuildArgumentType();
    }
}
