package com.campmongoose.serversaturday.forge;

import com.campmongoose.serversaturday.common.SQLTextSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

public class SQLTextComponentSerializer implements SQLTextSerializer<ITextComponent> {

    @Nonnull
    public List<ITextComponent> deserialize(@Nonnull String string) {
        JsonArray jsonArray = new Gson().fromJson(string, JsonArray.class);
        return StreamSupport.stream(jsonArray.spliterator(), false).map(Serializer::getComponentFromJson).collect(Collectors.toList());
    }

    @Nonnull
    public String serialize(@Nonnull List<ITextComponent> list) {
        JsonArray jsonArray = new JsonArray();
        list.stream().map(Serializer::toJsonTree).forEach(jsonArray::add);
        return jsonArray.toString();
    }
}
