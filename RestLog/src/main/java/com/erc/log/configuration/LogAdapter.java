package com.erc.log.configuration;

import com.erc.log.appenders.Appender;
import com.erc.log.appenders.AppenderFactory;
import com.erc.log.appenders.AppenderType;
import com.erc.log.appenders.BaseAppender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LogAdapter implements JsonDeserializer<LogConfiguration> {
    @Override
    public LogConfiguration deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        LogConfiguration logConfiguration = new Gson().fromJson(jsonElement.getAsJsonObject(), LogConfiguration.class);

        try {
            if (jsonElement.getAsJsonObject().get("level") != null) {
                Level level = null;
                level = Level.valueOf(jsonElement.getAsJsonObject().get("level").getAsString().toUpperCase());
                logConfiguration.setLevel(level);
            }

            if (jsonElement.getAsJsonObject().get("filters") != null) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Filter.class, new FilterAdapter());
                Gson gson1 = gsonBuilder.create();

                Type collectionType = new TypeToken<ArrayList<Filter>>() {
                }.getType();
                ArrayList<Filter> filters = gson1.fromJson(jsonElement.getAsJsonObject().get("filters").toString(), collectionType);

                logConfiguration.setFilters(filters);
            }

            if (jsonElement.getAsJsonObject().get("appenders") != null) {

                JsonArray items = jsonElement.getAsJsonObject().get("appenders").getAsJsonArray();

                ArrayList<BaseAppender> appenders = new ArrayList<>();
                for (JsonElement item : items) {
                    AppenderType appenderType = AppenderType.valueOf(item.getAsJsonObject().get("type").getAsString().toUpperCase());
                    Appender appender = AppenderFactory.getInstance(appenderType, item.toString());
                    appenders.add((BaseAppender) appender);
                }

                logConfiguration.setAppenders(appenders);
            }

        } catch (IllegalArgumentException ie) {
            System.out.println(ie.getMessage());
            System.out.println("level cannot be serialized ..");
        }

        return logConfiguration;
    }
}
