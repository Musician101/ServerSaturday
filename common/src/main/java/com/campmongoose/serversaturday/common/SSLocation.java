package com.campmongoose.serversaturday.common;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SSLocation {

    private double pitch;
    @NotNull
    private UUID world;
    private double x;
    private double y;
    private double yaw;
    private double z;

    public SSLocation(@NotNull UUID world) {
        this.world = world;
    }

    public SSLocation(@NotNull UUID world, double x, double y, double z, double pitch, double yaw) {
        this(world);
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    @NotNull
    public UUID getWorld() {
        return world;
    }

    public void setWorld(@NotNull UUID world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
