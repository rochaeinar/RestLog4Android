package com.erc.log.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

public class Display {
    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public static String getDpi(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.densityDpi + "dpi";
    }

    public static String getOrientation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }


    public static String getScreenResolution(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String layout;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                layout = "Large";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                layout = "Normal";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                layout = "Small";
                break;
            default:
                layout = "unknown";
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return layout + " " + width + " x " + height;
    }

    public static DisplayStatus getScreenStatus(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
                for (android.view.Display display : dm.getDisplays()) {
                    if (display.getState() != android.view.Display.STATE_OFF) {
                        return DisplayStatus.ON;
                    }
                }
            } else {
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (powerManager.isScreenOn()) {
                    return DisplayStatus.ON;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DisplayStatus.OFF;
    }

}
