package com.campmongoose.serversaturday.dialog;

import com.campmongoose.serversaturday.gui.BuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.stream.Stream;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class AllSubmissionsDialog extends SSDialog {

    public AllSubmissionsDialog() {
        super(Component.translatable("ss.gui.all-submissions.label"));
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = getPlugin().getSubmissions().getSubmitters().stream().flatMap(this::buttons).toList();
        return DialogType.multiAction(buttons, backButton((view, audience) -> {
        }), 2);
    }

    private Stream<ActionButton> buttons(Submitter submitter) {
        return submitter.builds().stream().filter(build -> !build.featured() && build.submitted()).map(build -> button(submitter, build));
    }

    private ActionButton button(Submitter submitter, Build build) {
        ComponentLike resolver = Argument.tagResolver(Placeholder.unparsed("build", build.name()), Placeholder.parsed("submitter", submitter.name()));
        Component label = Component.translatable("ss.gui.all-submissions.build", resolver);
        return actionButton(label, (view, audience) -> BuildGUI.open(build, submitter, (Player) audience));
    }
}
