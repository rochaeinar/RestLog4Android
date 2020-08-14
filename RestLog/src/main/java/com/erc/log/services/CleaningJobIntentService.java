package com.erc.log.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.SafeJobIntentService;

import com.erc.log.containers.FILE;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.Preferences;
import com.erc.log.model.FilesModel;
import com.erc.log.model.LogModel;

import java.util.ArrayList;
import java.util.Calendar;


public class CleaningJobIntentService extends SafeJobIntentService {

    private static final int JOB_ID = 1302;

    public static void clean(Context context) {
        int updatedDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if (Preferences.get(context, Preferences.OLDER_LOGS_DELETED, -1) != updatedDay) {
            Preferences.set(context, Preferences.OLDER_LOGS_DELETED, updatedDay);
            enqueueWork(context, CleaningJobIntentService.class, JOB_ID, new Intent());
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.w("CleaningJob", "STARTING");

        ArrayList<FILE> files = FilesModel.getFilesToDelete();

        for (FILE file : files) {
            if (FileHelper.exist(file.fullPath)) {
                if (FileHelper.deleteFile(file.fullPath)) {
                    FilesModel.deleteFile(file.id);
                    FileHelper.deleteFile(file.fullPath + "-journal");
                }
            } else {
                FilesModel.deleteFile(file.id);
            }
        }

        ArrayList<LOG> logs = LogModel.getLogsToDelete();

        for (LOG log : logs) {
            LogModel.delete(log.id);
        }
        Log.w("CleaningJob", "SUCCESS");
    }
}
