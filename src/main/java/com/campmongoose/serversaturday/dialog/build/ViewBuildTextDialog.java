package com.campmongoose.serversaturday.dialog.build;

import com.campmongoose.serversaturday.dialog.SSDialog;
import com.campmongoose.serversaturday.gui.ViewBuildGUI;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ViewBuildTextDialog extends SSDialog {

    private final Component text;
    private final ViewBuildGUI prevGUI;

    public ViewBuildTextDialog(Component label, String text, ViewBuildGUI prevGUI) {
        super(label);
        this.text = Component.text(text);
        this.prevGUI = prevGUI;
    }

    @Override
    protected List<DialogBody> body() {
        return List.of(DialogBody.plainMessage(text));
    }

    @Override
    protected DialogType type() {
        return DialogType.notice(backButton((view, audience) -> prevGUI.open()));
    }
}
