package com.erc.log.helpers;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.UUID;

public class AndroidId {
    public static String getUniquePseudoID(Context context) {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String android_id = null;
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            return new UUID(android_id.hashCode(), m_szDevIDShort.hashCode()).toString();
        } catch (Exception exception) {
            android_id = "android_id";
        }

        return new UUID(m_szDevIDShort.hashCode(), android_id.hashCode()).toString();
    }
}
