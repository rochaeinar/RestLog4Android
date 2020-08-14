package com.erc.log.appenders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppenderFactory {
    public static Appender getInstance(AppenderType appenderType, String json) {

        Gson gson = new GsonBuilder().create();
        Appender appender = null;

        switch (appenderType) {
            case REST:
                appender = gson.fromJson(json, RestAppender.class);
                break;
            case CONSOLE:
                appender = gson.fromJson(json, ConsoleAppender.class);
                break;
            case LOGCAT:
                appender = gson.fromJson(json, LogcatAppender.class);
                break;
            case DATABASE:
                appender = gson.fromJson(json, DataBaseAppender.class);
                break;
            case FILE:
                appender = gson.fromJson(json, FileAppender.class);
                break;
        }
        appender.setType(appenderType);
        return appender;
    }
}
