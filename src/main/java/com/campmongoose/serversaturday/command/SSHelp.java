package com.campmongoose.serversaturday.command;

import com.mojang.brigadier.context.CommandContext;
import io.musician101.musicommand.paper.command.PaperLiteralCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

import static com.campmongoose.serversaturday.command.SSMain.help;

@NullMarked
public class SSHelp implements PaperLiteralCommand.AdventureFormat, SSCommand {

    private final SSMain root;

    public SSHelp(SSMain root) {
        this.root = root;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public Integer execute(CommandContext<CommandSourceStack> context) {
        help(context, root);
        return 1;
    }
}
