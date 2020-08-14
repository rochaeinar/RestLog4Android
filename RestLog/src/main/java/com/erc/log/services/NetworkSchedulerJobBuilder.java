package com.erc.log.services;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.erc.log.services.SynchronizationJobIntentService;


public class NetworkSchedulerJobBuilder {

    private final static int JOB_ID = 100;

    public static void scheduleJob(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJobUsingSpecificAPI(context.getApplicationContext());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void scheduleJobUsingSpecificAPI(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(context, NetworkSchedulerJobService.class))
                .setMinimumLatency(15000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        scheduler.schedule(job);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static class NetworkSchedulerJobService extends JobService {

        public NetworkSchedulerJobService() {
            super();
        }

        @Override
        public boolean onStartJob(JobParameters params) {
            SynchronizationJobIntentService.synchronize(getApplicationContext());
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters params) {
            return false;
        }
    }
}

