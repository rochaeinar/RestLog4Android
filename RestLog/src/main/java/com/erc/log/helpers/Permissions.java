package com.erc.log.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.erc.log.R;

import pub.devrel.easypermissions.EasyPermissions;

public class Permissions {
    private static int REQUEST_PERMISSION_LOCATION = 1067;

    private static String[] storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean hasStoragePermission(Context context) {
        return EasyPermissions.hasPermissions(context, storagePermissions);

    }

    public static void requestStoragePermission(Activity activity) {
        EasyPermissions.requestPermissions(
                activity,
                activity.getApplicationContext().getString(R.string.storage_permission),
                REQUEST_PERMISSION_LOCATION, storagePermissions);
    }


}
