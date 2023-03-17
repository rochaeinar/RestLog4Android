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
import com.erc.log.helpers.DateHelper;
import com.erc.log.helpers.FileHelper;
import com.erc.log.model.FilesModel;
import com.erc.log.model.LogModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class MoveLogsJobIntentService extends SafeJobIntentService {

    private static final int JOB_ID = 1302;

    public static void moveFilesFromYesterday(Context context) {
        Intent intent = new Intent();
        intent.putExtra("copy", false);
        enqueueWork(context, MoveLogsJobIntentService.class, JOB_ID, intent);
    }

    public static void copyTodaysFiles(Context context) {
        Intent intent = new Intent();
        intent.putExtra("copy", true);
        enqueueWork(context, MoveLogsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.w(Constants.TAG, "MoveLogsJob: STARTING");
        FileAppender fileAppender = ((FileAppender) LogConfiguration
                .getInstance(AppContext.getContext())
                .getAppender(AppenderType.FILE));
        if (fileAppender != null) {
            boolean copy = intent.getBooleanExtra("copy", false);
            if (copy) {
                copy(fileAppender);
            } else {
                moveAndDeleteLogs(fileAppender);
            }
        }
    }

    private void moveAndDeleteLogs(FileAppender fileAppender) {
        String relativePath = fileAppender.getPath();
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

    private void copy(FileAppender fileAppender) {
        String relativePath = fileAppender.getPath();
        ArrayList<FILE> files = FilesModel.getFilesToCopy();
        File downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        for (FILE file : files) {
            if (FileHelper.exist(file.fullPath)) {
                String fileName = FileHelper.removeExtension(FileHelper.getFileName(file.fullPath));
                String extension = FileHelper.getExtension(file.fullPath);
                String time = DateHelper.getDateWithFormat(new Date(), "_hh.mm.ss");
                FileHelper.copyFileChangingName(file.fullPath,
                        downloadsPath.getPath() + "/" + relativePath,
                        fileName + time + "." + extension);
            }
        }
        Log.w(Constants.TAG, "CopyLogsJob: files " + files.size());
    }
}
