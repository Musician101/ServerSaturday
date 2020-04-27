package com.campmongoose.serversaturday.forge.gui.chest;

import com.campmongoose.serversaturday.common.Reference.MenuText;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.gui.ForgeBookGUI;
import com.campmongoose.serversaturday.forge.gui.ForgeIconBuilder;
import com.campmongoose.serversaturday.forge.network.SSNetwork;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.Location;
import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import javax.annotation.Nonnull;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.bukkit.event.inventory.ClickType;

//TODO replace with Forge GUIs
@Deprecated
public class EditBuildScreen extends BuildScreen {

    private CheckboxButton submitted;
    private Location location;
    private TextFieldWidget name;
    private TextFieldWidget description;
    private TextFieldWidget resourcePacks;

    public EditBuildScreen(@Nonnull ForgeBuild build) {
        super(build, 7, 5);
        setButton(2, ForgeIconBuilder.builder(Items.BOOK).name(MenuText.CHANGE_DESCRIPTION_NAME).description(MenuText.CHANGE_DESCRIPTION_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (!itemStack.isEmpty()) {
                p.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (ForgeBookGUI.isEditing(p)) {
                p.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            p.closeInventory();
            new ForgeBookGUI(p, build, build.getDescription(), (ply, pages) -> {
                build.setDescription(pages);
                new EditBuildScreen(build, submitter, ply);
            });
        }));
        setButton(3, ForgeIconBuilder.builder(Items.PAINTING).name(MenuText.CHANGE_RESOURCE_PACKS_NAME).description(MenuText.CHANGE_RESOURCES_PACK_DESC).build(), ImmutableMap.of(ClickType.LEFT, p -> {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (!itemStack.isEmpty()) {
                p.sendMessage(ChatColor.RED + Messages.HAND_NOT_EMPTY);
                return;
            }

            if (ForgeBookGUI.isEditing(p)) {
                p.sendMessage(ChatColor.RED + Messages.EDIT_IN_PROGRESS);
                return;
            }

            p.closeInventory();
            new ForgeBookGUI(p, build, build.getResourcePacks(), (ply, pages) -> {
                build.setResourcePacks(pages);
                new EditBuildScreen(build, submitter, ply);
            });
        }));
        setButton(8, ForgeIconBuilder.of(Items.BARRIER, "Back"), ImmutableMap.of(ClickType.LEFT, p -> new SubmitterScreen(submitter, p)));
    }

    @Override
    protected void init() {
        super.init();
        name = new TextFieldWidget(font, width / 2 - 100, 22, 200, 20, "Name");
        name.setText(build.getName());
        Button location = addButton(new Button(width / 2 - 100, (name.y + name.getHeight() + 10) / 4 + 149, 98, 20, MenuText.CHANGE_LOCATION_NAME, button -> {
            ClientPlayerEntity player = minecraft.player;
            build.setLocation(new Location(player));
            player.sendMessage(new StringTextComponent(Messages.locationChanged(build)).applyTextStyle(TextFormatting.GREEN));
        }));
        submitted = addButton(new CheckboxButton(width / 2 + 100, location.y, 98, 20, "Submitted?", build.submitted()));
        description = new TextFieldWidget(font, width / 2 - 100, submitted.y + 20, 98, height - 50, String.join("\n", MenuText.DESCRIPTION_NAME));

    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        renderBackground();
        drawCenteredString(font, title.getFormattedText(), width / 2, 20, Color.WHITE.getRGB());
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    public void onClose() {
        ClientPlayerEntity player = minecraft.player;
        NetworkManager networkManager = player.connection.getNetworkManager();
        SimpleChannel network = SSNetwork.INSTANCE;
        NetworkDirection direction = NetworkDirection.PLAY_TO_SERVER;
        network.sendTo(build, networkManager, direction);
    }
}
