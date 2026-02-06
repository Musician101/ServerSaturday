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

        node.set(obj.serialize());
    }
}
