package com.erc.log.services;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.app.SafeJobIntentService;
import android.util.Log;

import com.erc.log.Constants;
import com.erc.log.DataBase;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.Network;
import com.erc.log.http.LogResource;

import java.util.ArrayList;


public class SynchronizationJobIntentService extends SafeJobIntentService {

    private static final int JOB_ID = 1301;

    public static void synchronize(Context context) {
        enqueueWork(context, SynchronizationJobIntentService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        SystemClock.sleep(10000);

        if (intent != null && Network.hasConnectivity(getApplicationContext())) {
            Log.w(Constants.TAG, "onHandleWork: STARTING");

            LogResource logResource = new LogResource();
            ArrayList<LOG> logs = DataBase.getInstance().getAll(LOG.class);

            for (LOG log : logs) {
                LOG result = logResource.post(log);
                if (result != null) {
                    DataBase.getInstance().remove(LOG.class, log.getId());
                }
            }
            Log.w(Constants.TAG, "onHandleWork: SUCCESS");
        }
    }
}
