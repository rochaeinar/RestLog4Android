package com.erc.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.erc.log.helpers.AlarmHelper;
import com.erc.log.services.MoveLogsJobIntentService;

public class DailyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(Constants.TAG, "DailyReceiver");
        AlarmHelper.setAlarm(context);
        MoveLogsJobIntentService.moveFilesFromYesterday(AppContext.getContext());
    }
}
