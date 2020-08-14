package com.erc.log.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ApplicationInformation {

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }


    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static String getApplicationVersion(Context context) {
        String version = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(context), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
