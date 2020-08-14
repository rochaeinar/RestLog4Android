package com.erc.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.erc.log.services.SynchronizationJobIntentService;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SynchronizationJobIntentService.synchronize(context);
    }
}
