package com.erc.log;

import android.content.Context;

public class AppContext {
    private Context context = null;
    private static AppContext appContext = null;

    public static Context getContext() {
        if (appContext == null) {
            appContext = new AppContext();
        }
        return appContext.context;
    }

    public static void setContext(Context context) {
        if (appContext == null) {
            appContext = new AppContext();
        }
        appContext.context = context;
    }

    private AppContext() {
    }
}
