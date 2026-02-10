package com.campmongoose.serversaturday.dialog;

import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static com.campmongoose.serversaturday.ServerSaturday.getPlugin;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class SubmittersDialog extends SSDialog {

    public SubmittersDialog() {
        super(Component.translatable("ss.gui.submitters.label"));
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = getPlugin().getSubmissions().getSubmitters().stream().map(this::button).toList();
        return DialogType.multiAction(buttons, backButton((view, audience) -> {
        }), 2);
    }

    private ActionButton button(Submitter submitter) {
        ComponentLike argument = Argument.tagResolver(Placeholder.unparsed("submitter", submitter.name()));
        Component label = Component.translatable("ss.gui.submitters.submitter", argument);
        return actionButton(label, (view, audience) -> audience.showDialog(new SubmitterDialog(submitter).build()));
    }
}
