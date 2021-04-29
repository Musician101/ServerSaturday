package com.campmongoose.serversaturday.spigot;

import com.campmongoose.serversaturday.common.SQLTextSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;

public class SQLStringSerializer implements SQLTextSerializer<String> {

    @Nonnull
    public List<String> deserialize(@Nonnull String string) {
        return StreamSupport.stream(new Gson().fromJson(string, JsonArray.class).spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
    }

    @Nonnull
    public String serialize(@Nonnull List<String> list) {
        JsonArray jsonArray = new JsonArray();
        list.forEach(jsonArray::add);
        return new Gson().toJson(jsonArray);
    }
}
