package com.campmongoose.serversaturday.dialog;

import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ErrorDialog extends SSDialog {

    private final Component errorMessage;
    private final SSDialog previousDialog;

    ErrorDialog(Component errorMessage, SSDialog previousDialog) {
        super(Component.text("An error has occurred!", NamedTextColor.RED));
        this.errorMessage = errorMessage;
        this.previousDialog = previousDialog;
    }

    @Override
    protected List<DialogBody> body() {
        return List.of(DialogBody.plainMessage(errorMessage));
    }

    @Override
    protected DialogType type() {
        return DialogType.notice(backButton((view, audience) -> audience.showDialog(previousDialog.build())));
    }
}
