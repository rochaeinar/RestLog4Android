package com.erc.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.erc.log.helpers.AlarmHelper;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(Constants.TAG, "BootReceiver: onReceive");
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            AlarmHelper.setAlarm(context);
        }
    }
}