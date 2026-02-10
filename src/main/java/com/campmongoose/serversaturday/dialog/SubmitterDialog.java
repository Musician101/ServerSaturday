package com.campmongoose.serversaturday.dialog;

import com.campmongoose.serversaturday.gui.BuildGUI;
import com.campmongoose.serversaturday.submission.Build;
import com.campmongoose.serversaturday.submission.Submitter;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class SubmitterDialog extends SSDialog {

    private final Submitter submitter;

    public SubmitterDialog(Submitter submitter) {
        super(Component.translatable("ss.gui.submitter.label", Argument.tagResolver(Placeholder.unparsed("submitter", submitter.name()))));
        this.submitter = submitter;
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = submitter.builds().stream().map(this::button).toList();
        return DialogType.multiAction(buttons, backButton((view, audience) -> audience.showDialog(new SubmittersDialog().build())), 2);
    }

    private ActionButton button(Build build) {
        ComponentLike argument = Argument.tagResolver(Placeholder.unparsed("build", build.name()), Formatter.booleanChoice("submitted-and-not-featured", build.submitted() && !build.featured()));
        Component label = Component.translatable("ss.gui.submitter.build", argument);
        return actionButton(label, (view, audience) -> BuildGUI.open(build, submitter, (Player) audience));
    }
}
