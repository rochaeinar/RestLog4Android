package com.erc.log.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.erc.log.Constants;
import com.erc.log.DailyReceiver;

import java.util.Calendar;

import pub.devrel.easypermissions.EasyPermissions;

public class AlarmHelper {

    private static String[] alarmPermissions = new String[]{android.Manifest.permission.SCHEDULE_EXACT_ALARM};

    public static void setAlarm(Context context) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        long time = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 235, intent, FlagsHelper.getIntentFlag());

        if (hasExactAlarmPermission(context.getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pIntent);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pIntent);
            }
        }
        Log.w(Constants.TAG, "SetAlarmAt: " + calendar.getTime().toString());
    }

    public static boolean hasExactAlarmPermission(Context context) {
        return EasyPermissions.hasPermissions(context, alarmPermissions);
    }
}
