package com.campmongoose.serversaturday.sponge;

import com.campmongoose.serversaturday.common.SQLTextSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class SQLComponentSerializer implements SQLTextSerializer<Component> {

    @Nonnull
    public List<Component> deserialize(@Nonnull String string) {
        GsonComponentSerializer gcs = GsonComponentSerializer.gson();
        Gson gson = gcs.serializer();
        return StreamSupport.stream(gson.fromJson(string, JsonArray.class).spliterator(), false).map(JsonElement::toString).map(gcs::deserialize).collect(Collectors.toList());
    }

    @Nonnull
    public String serialize(@Nonnull List<Component> list) {
        GsonComponentSerializer gcs = GsonComponentSerializer.gson();
        Gson gson = gcs.serializer();
        JsonArray jsonArray = new JsonArray();
        list.stream().map(gcs::serializeToTree).forEach(jsonArray::add);
        return gson.toJson(jsonArray);
    }
}
