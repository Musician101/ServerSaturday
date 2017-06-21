package com.campmongoose.serversaturday.sponge.command.view;

import com.campmongoose.serversaturday.sponge.command.AbstractSpongeCommand;
import com.campmongoose.serversaturday.sponge.command.args.SubmitterCommandElement;
import com.campmongoose.serversaturday.sponge.submission.SpongeBuild;
import com.campmongoose.serversaturday.sponge.submission.SpongeSubmitter;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;

public class SSViewDescription extends AbstractSpongeCommand {

    @Nonnull
    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext arguments) {
        return arguments.<Entry<SpongeSubmitter, SpongeBuild>>getOne(SubmitterCommandElement.KEY).map(entry -> {
            if (source instanceof Player) {
                Player player = (Player) source;
                SpongeSubmitter submitter = entry.getKey();
                SpongeBuild build = entry.getValue();
                BookView.Builder bvb = BookView.builder();
                bvb.author(Text.of(submitter.getName()));
                bvb.addPages(build.getDescription().stream().map(Text::of).collect(Collectors.toList()));
                bvb.title(Text.of(build.getName()));
                player.sendBookView(bvb.build());
                return CommandResult.success();
            }

            return playerOnly(source);
        }).orElse(CommandResult.empty());
    }
}
