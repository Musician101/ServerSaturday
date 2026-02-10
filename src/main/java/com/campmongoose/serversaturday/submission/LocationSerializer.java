package com.campmongoose.serversaturday.submission;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@NullMarked
public class LocationSerializer implements TypeSerializer<Location> {

    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Map<String, Object> map = new HashMap<>();
        for (Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            // In theory, ConfigurationNode#get() will never be null.
            //noinspection DataFlowIssue
            map.put(entry.getKey().toString(), entry.getValue().get(Object.class));
        }

        try {
            return Location.deserialize(map);
        }
        catch (IllegalArgumentException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            return;
        }

        // For some reason, it can't write the float values when calling Location#serialize()
        // So we have to manually set everything instead.
        node.node("world").set(obj.getWorld().getName());
        node.node("x").set(obj.x());
        node.node("y").set(obj.y());
        node.node("z").set(obj.z());
        node.node("yaw").set(Float.class, obj.getYaw());
        node.node("pitch").set(Float.class, obj.getPitch());
    }
}
