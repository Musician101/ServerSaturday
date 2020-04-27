package com.campmongoose.serversaturday.forge.network;

import com.campmongoose.serversaturday.common.Reference;
import com.campmongoose.serversaturday.common.Reference.Messages;
import com.campmongoose.serversaturday.forge.ForgeServerSaturday;
import com.campmongoose.serversaturday.forge.gui.chest.EditBuildScreen;
import com.campmongoose.serversaturday.forge.gui.chest.SubmitterScreen;
import com.campmongoose.serversaturday.forge.gui.chest.ViewBuildScreen;
import com.campmongoose.serversaturday.forge.network.message.ViewBuildMessage;
import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmissions;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmitter;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class SSNetwork {

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.ID, "network"), () -> Reference.VERSION, s -> s.equals(Reference.VERSION), s -> s.equals(Reference.VERSION));
    private static int id = 0;

    private SSNetwork() {

    }

    public static void init() {
        INSTANCE.registerMessage(id++, ForgeBuild.class, (build, packetBuffer) -> packetBuffer.writeString(ForgeSubmissions.GSON.toJson(build)), packetBuffer -> ForgeSubmissions.GSON.fromJson(packetBuffer.readString(32767), ForgeBuild.class), (build, supplier) -> {
            Context context = supplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new EditBuildScreen(build));
            }
            else {
                ServerPlayerEntity player = context.getSender();
                if (player != null) {
                    ForgeSubmitter submitter = ForgeServerSaturday.getInstance().getSubmissions().getSubmitter(player);
                    submitter.updateBuild(build);
                    player.sendMessage(new StringTextComponent(Messages.PREFIX + "Build updated.").applyTextStyle(TextFormatting.GREEN));
                    INSTANCE.sendTo(submitter, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                }
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, ViewBuildMessage.class, ViewBuildMessage::writeToBuffer, ViewBuildMessage::new, (message, supplier) -> {
            Context context = supplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new ViewBuildScreen(message));
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, ForgeSubmitter.class, (submitter, packetBuffer) -> packetBuffer.writeString(ForgeSubmissions.GSON.toJson(submitter)), packetBuffer -> ForgeSubmissions.GSON.fromJson(packetBuffer.readString(32767), ForgeSubmitter.class), (submitter, supplier) -> {
            Context context = supplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) {
                Minecraft.getInstance().displayGuiScreen(new SubmitterScreen(submitter));
            }

            context.setPacketHandled(true);
        });
        INSTANCE.registerMessage(id++, UUID.class, (uuid, packetBuffer) -> packetBuffer.writeUniqueId(uuid), PacketBuffer::readUniqueId, (uuid, supplier) -> {
            Context context = supplier.get();
            if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT) {
                ServerPlayerEntity player = context.getSender();
                if (player != null) {
                    INSTANCE.sendTo(ForgeServerSaturday.getInstance().getSubmissions().getSubmitter(uuid), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                }
            }

            context.setPacketHandled(true);
        });
    }
}
