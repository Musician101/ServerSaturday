package com.campmongoose.serversaturday.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.stream.Collector;

public class JsonUtils {

    private JsonUtils() {

    }

    public static Collector<JsonElement, JsonArray, JsonArray> jsonArrayElementCollector() {
        return Collector.of(JsonArray::new, JsonArray::add, (left, right) -> {
            left.addAll(right);
            return left;
        });
    }

    public static Collector<String, JsonArray, JsonArray> jsonArrayStringCollector() {
        return Collector.of(JsonArray::new, JsonArray::add, (left, right) -> {
            left.addAll(right);
            return left;
        });
    }
}
