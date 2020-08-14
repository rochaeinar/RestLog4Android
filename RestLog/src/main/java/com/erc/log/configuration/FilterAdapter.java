package com.erc.log.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FilterAdapter implements JsonDeserializer<Filter> {
    @Override
    public Filter deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Filter filter = new Gson().fromJson(jsonElement.getAsJsonObject(), Filter.class);

        try {
            FilterOperator filterOperator = null;
            if (jsonElement.getAsJsonObject().get("filterOperator") != null) {
                filterOperator = FilterOperator.valueOf(jsonElement.getAsJsonObject().get("filterOperator").getAsString().toUpperCase());
            }
            filter.setFilterOperator(filterOperator);

        } catch (IllegalArgumentException ie) {
            System.out.println(ie.getMessage());
            System.out.println("filterOperator cannot be serialized ..");
        }

        return filter;
    }
}
