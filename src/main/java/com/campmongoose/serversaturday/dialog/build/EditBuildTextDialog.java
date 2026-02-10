package com.campmongoose.serversaturday.dialog.build;

import com.campmongoose.serversaturday.dialog.SSDialog;
import com.campmongoose.serversaturday.submission.Build;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput.MultilineOptions;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public abstract class EditBuildTextDialog extends SSDialog {

    private static final String TEXT = "text";
    private final String original;

    private EditBuildTextDialog(Component label, String original) {
        super(label);
        this.original = original;
    }

    public static Dialog rename(Build build) {
        return new EditBuildTextDialog(Component.text("Set the name of your build."), build.name()) {

            @Override
            protected void action(String string) {
                build.name(string);
            }
        }.build();
    }

    public static Dialog changeDescription(Build build) {
        return new EditBuildTextDialog(Component.text("Enter your new description."), build.description()) {

            @Override
            protected void action(String string) {
                build.description(string);
            }

            @Override
            protected MultilineOptions multilineOptions() {
                return MultilineOptions.create(512, 100);
            }

            @Override
            protected int maxLength() {
                return 512;
            }
        }.build();
    }

    public static Dialog changeResourcePack(Build build) {
        return new EditBuildTextDialog(Component.text("Enter the preferred resource pack."), build.resourcePack()) {

            @Override
            protected void action(String string) {
                build.resourcePack(string);
            }
        }.build();
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput text = DialogInput.text(TEXT, Component.empty()).initial(original).maxLength(maxLength()).multiline(multilineOptions()).labelVisible(false).build();
        return List.of(text);
    }

    @Override
    protected DialogType type() {
        ActionButton confirm = confirmButton((view, audience) -> {
            String text = view.getText(TEXT);
            if (text == null) {
                showError(audience, Component.text("The text you entered is null. This is most likely a bug."));
                return;
            }

            action(text);
        });
        ActionButton back = backButton((view, audience) -> audience.closeDialog());
        return DialogType.confirmation(confirm, back);
    }

    protected abstract void action(String string);

    protected @Nullable MultilineOptions multilineOptions() {
        return null;
    }

    protected int maxLength() {
        return 32;
    }
}
