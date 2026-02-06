package com.campmongoose.serversaturday.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public abstract class SSDialog {

    private static final ClickCallback.Options DEFAULT_CALLBACK_OPTIONS = ClickCallback.Options.builder().uses(1).lifetime(ClickCallback.DEFAULT_LIFETIME).build();

    protected final Component label;

    public SSDialog(Component label) {
        this.label = label;
    }

    public Dialog build() {
        return Dialog.create(b -> b.empty().type(type()).base(base()));
    }

    protected DialogAction customClick(DialogActionCallback callback) {
        return DialogAction.customClick(callback, DEFAULT_CALLBACK_OPTIONS);
    }

    protected ActionButton backButton(DialogActionCallback callback) {
        return actionButton(Component.text("Back"), callback);
    }

    protected ActionButton confirmButton(DialogActionCallback callback) {
        return actionButton(Component.text("Confirm"), callback);
    }

    protected ActionButton actionButton(Component label, DialogActionCallback callback) {
        return ActionButton.builder(label).action(customClick(callback)).build();
    }

    protected DialogBase base() {
        return DialogBase.builder(label).externalTitle(label).inputs(inputs()).body(body()).build();
    }

    protected List<DialogInput> inputs() {
        return List.of();
    }

    protected abstract DialogType type();

    protected List<DialogBody> body() {
        return List.of();
    }

    protected void showError(Audience audience, Component error) {
        audience.showDialog(new ErrorDialog(error, this).build());
    }
}
