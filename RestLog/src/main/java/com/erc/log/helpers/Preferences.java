package com.erc.log.helpers;

import android.content.Context;
import android.util.Log;

public class Preferences {

    public static String SETTINGS_NAME = "logger_settings";
    public static String JSON_CONFIGURATION = "json_configuration";
    public static String JSON_CONFIGURATION_ASSETS = "json_configuration_assets";
    public static String OLDER_LOGS_DELETED = "older_logs_deleted";


    public static String get(Context context, String name, String... default_) {
        String res = null;
        String default1 = default_.length == 0 ? "" : default_[0];
        try {
            res = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).getString(name, default1);
        } catch (Exception e) {
            res = default1;
            Log.e("SharedPreferences", "ERROR: get_" + name + "()");
        }
        return res;
    }

    public static int get(Context context, String name, int... default_) {
        int res = 0;
        int default1 = default_.length == 0 ? 0 : default_[0];
        try {
            res = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).getInt(name, default1);
        } catch (Exception e) {
            res = default1;
            Log.e("SharedPreferences", "ERROR: get_" + name + "()");
        }
        return res;
    }

    public static Boolean get(Context context, String name, Boolean... default_) {
        Boolean res = false;
        Boolean default1 = default_.length == 0 ? false : default_[0];
        try {
            res = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).getBoolean(name, default1);
        } catch (Exception e) {
            res = default1;
            Log.e("SharedPreferences", "ERROR: get_" + name + "()");
        }
        return res;
    }

    public static void set(Context context, String name, String value) {
        try {
            context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).edit().putString(name, value).apply();
        } catch (Exception e) {
            Log.e("SharedPreferences", "ERROR: set_" + name + "()");
        }
    }

    public static void set(Context context, String name, int value) {
        try {
            context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).edit().putInt(name, value).apply();
        } catch (Exception e) {
            Log.e("SharedPreferences", "ERROR: set_" + name + "()");
        }
    }

    public static void set(Context context, String name, boolean value) {
        try {
            context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE).edit().putBoolean(name, value).apply();
        } catch (Exception e) {
            Log.e("SharedPreferences", "ERROR: set_" + name + "()");
        }
    }
}
