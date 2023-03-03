package com.erc.log.services;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.SafeJobIntentService;

import com.erc.log.AppContext;
import com.erc.log.Constants;
import com.erc.log.appenders.AppenderType;
import com.erc.log.appenders.FileAppender;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.FILE;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.FileHelper;
import com.erc.log.model.FilesModel;
import com.erc.log.model.LogModel;

import java.io.File;
import java.util.ArrayList;


public class MoveLogsJobIntentService extends SafeJobIntentService {

    private static final int JOB_ID = 1302;

    public static void move(Context context) {
        enqueueWork(context, MoveLogsJobIntentService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.w(Constants.TAG, "MoveLogsJob: STARTING");

        String relativePath = ((FileAppender) LogConfiguration
                .getInstance(AppContext.getContext())
                .getAppender(AppenderType.FILE)).getPath();
        ArrayList<FILE> files = FilesModel.getFilesToMove();
        File downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        for (FILE file : files) {
            if (FileHelper.exist(file.fullPath)) {
                FileHelper.copyFile(file.fullPath, downloadsPath.getPath() + "/" + relativePath);
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
        Log.w(Constants.TAG, "MoveLogsJob: files " + files.size());
    }
}
