package com.campmongoose.serversaturday.sponge.command;

import com.campmongoose.serversaturday.common.command.AbstractCommandUsage;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class SpongeCommandUsage extends AbstractCommandUsage<Text, SpongeCommandArgument>
{
    public SpongeCommandUsage(List<SpongeCommandArgument> arguments)
    {
        super(arguments);
    }

    public SpongeCommandUsage(List<SpongeCommandArgument> arguments, int minArgs)
    {
        super(arguments, minArgs);
    }

    @Override
    protected Text parseUsage(List<SpongeCommandArgument> arguments)
    {
        List<Text> usageText = new ArrayList<>();
        usageText.add(arguments.get(0).format().toBuilder().color(TextColors.GRAY).build());
        if (arguments.size() > 1)
            usageText.add(Text.of(arguments.get(1).format()));

        if (arguments.size() > 2)
            for (int x = 2; x < arguments.size() - 1; x++)
                usageText.add(arguments.get(x).format().toBuilder().color(TextColors.GREEN).build());

        return Text.joinWith(Text.of(" "), usageText);
    }
}
