package com.erc.log.configuration;

import android.content.Context;

import com.erc.log.AppContext;
import com.erc.log.appenders.AppenderType;
import com.erc.log.appenders.BaseAppender;
import com.erc.log.helpers.AssetsHelper;
import com.erc.log.helpers.Preferences;
import com.erc.log.helpers.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class LogConfiguration {
    private boolean enabled;
    private boolean avoidDuplicated;
    private int maxFileSizeMb;
    private long maxRecordNumber;
    private Level level;
    private int deleteAfter;

    private ArrayList<BaseAppender> appenders;
    private ArrayList<Filter> filters;

    private static final String FILE_NAME = "rest-log.json";

    private static LogConfiguration ourInstance;

    public static LogConfiguration getInstance(Context context) {
        if (ourInstance == null) {
            loadConfiguration(context);
        }
        return ourInstance;
    }

    public static void clear() {
        ourInstance = null;
    }

    private LogConfiguration() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        saveConfiguration();
    }

    public boolean isAvoidDuplicated() {
        return avoidDuplicated;
    }

    public void setAvoidDuplicated(boolean avoidDuplicated) {
        this.avoidDuplicated = avoidDuplicated;
        saveConfiguration();
    }

    public int getMaxFileSizeMb() {
        return maxFileSizeMb;
    }

    public void setMaxFileSizeMb(int maxFileSizeMb) {
        this.maxFileSizeMb = maxFileSizeMb;
        saveConfiguration();
    }

    public long getMaxRecordNumber() {
        return maxRecordNumber;
    }

    public void setMaxRecordNumber(long maxRecordNumber) {
        this.maxRecordNumber = maxRecordNumber;
        saveConfiguration();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getDeleteAfter() {
        return deleteAfter;
    }

    public void setDeleteAfter(int deleteAfter) {
        this.deleteAfter = deleteAfter;
    }

    public ArrayList<BaseAppender> getAppenders() {
        return appenders;
    }

    public BaseAppender getAppender(AppenderType appenderType) {

        for (BaseAppender appender : appenders) {
            if (appender.getType() == appenderType) {
                return appender;
            }
        }
        return null;
    }

    public void addAppender(BaseAppender appender) {
        this.appenders.add(appender);
    }

    public void setAppenders(ArrayList<BaseAppender> appenders) {
        this.appenders = appenders;
    }

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(Filter filter) {
        this.filters.add(filter);
    }

    private static LogConfiguration loadDefaultConfiguration() {
        return new LogConfiguration();
    }

    private static void loadConfiguration(Context context) {
        String jsonFile = null;
        String jsonFilePreferences = Preferences.get(context, Preferences.JSON_CONFIGURATION, "");
        String jsonFileAssets = AssetsHelper.readTextFromAssets(context, FILE_NAME);
        String jsonFileAssetsPreference = Preferences.get(AppContext.getContext(), Preferences.JSON_CONFIGURATION_ASSETS, "");

        if (StringUtil.isNullOrEmpty(jsonFilePreferences) && StringUtil.isNullOrEmpty(jsonFileAssets)) {
            ourInstance = loadDefaultConfiguration();
        } else {
            if (isANewJsonFileVersion(jsonFileAssets, jsonFileAssetsPreference)) {
                Preferences.set(AppContext.getContext(), Preferences.JSON_CONFIGURATION_ASSETS, jsonFileAssets);
                if (!StringUtil.isNullOrEmpty(jsonFileAssets)) {
                    jsonFile = jsonFileAssets;
                } else {
                    jsonFile = jsonFilePreferences;
                }
            } else {
                if (!StringUtil.isNullOrEmpty(jsonFilePreferences)) {
                    jsonFile = jsonFilePreferences;
                } else {
                    jsonFile = jsonFileAssets;
                }
            }
        }

        if (jsonFile != null) {
            String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            Gson gson = new GsonBuilder()
                    .setDateFormat(dateFormat)
                    .registerTypeAdapter(LogConfiguration.class, new LogAdapter())
                    .create();
            ourInstance = gson.fromJson(jsonFile, LogConfiguration.class);
        }
    }

    private static boolean isANewJsonFileVersion(String jsonFileAssets, String jsonFileAssetsPreference) {
        return !(StringUtil.isNullOrEmpty(jsonFileAssets)
                || jsonFileAssetsPreference.equalsIgnoreCase(jsonFileAssets));
    }

    private void saveConfiguration() {
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        String jsonString = gson.toJson(ourInstance);
        Preferences.set(AppContext.getContext(), Preferences.JSON_CONFIGURATION, jsonString);
        loadConfiguration(AppContext.getContext());
    }
}
