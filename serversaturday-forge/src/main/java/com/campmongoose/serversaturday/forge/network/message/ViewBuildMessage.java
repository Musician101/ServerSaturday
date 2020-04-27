package com.campmongoose.serversaturday.forge.network.message;

import com.campmongoose.serversaturday.forge.submission.ForgeBuild;
import com.campmongoose.serversaturday.forge.submission.ForgeSubmissions;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.network.PacketBuffer;

public class ViewBuildMessage {

    @Nonnull
    public final ForgeBuild build;
    @Nonnull
    public final UUID viewer;

    public ViewBuildMessage(@Nonnull PacketBuffer packetBuffer) {
        viewer = packetBuffer.readUniqueId();
        build = ForgeSubmissions.GSON.fromJson(packetBuffer.readString(32767), ForgeBuild.class);
    }

    public void writeToBuffer(@Nonnull PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(viewer).writeString(ForgeSubmissions.GSON.toJson(build));
    }
}
