package com.erc.log.appenders;

import android.os.Environment;

import com.erc.log.AppContext;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.DateHelper;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.Permissions;
import com.erc.log.helpers.StringUtil;
import com.erc.log.helpers.TextFileHelper;
import com.erc.log.model.FilesModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Date;

public class FileAppender extends BaseAppender {

    private String nameFormat;
    private String format;
    private String path;

    public FileAppender() {
    }

    @Override
    public void append(LOG log) {
        if (Permissions.hasStoragePermission(AppContext.getContext())) {
            Gson gson;
            String json;
            String fileName = getFileName();
            boolean existFile = FileHelper.exist(fileName);
            if (!existFile) {
                FilesModel.addFile(fileName);
            }
            if (FileHelper.getSize(fileName) < LogConfiguration.getInstance(AppContext.getContext()).getMaxFileSizeMb() * 1000) {
                switch (FileType.valueOf(format.toUpperCase())) {
                    case TXT:
                        TextFileHelper.write(fileName, log.toString(), true);
                        break;
                    case JSON:
                        if (existFile) {
                            TextFileHelper.deleteLastLine(fileName);
                        } else {
                            TextFileHelper.write(fileName, "[\n");
                        }

                        gson = new Gson();
                        json = StringUtil.format("{0}{1}\n]", existFile ? "," : "", gson.toJson(log));
                        TextFileHelper.write(fileName, json, true);
                        break;
                    case XML:
                        gson = new Gson();
                        json = gson.toJson(log);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String xml = StringUtil.format("{0}{1}{2}", "<log>", XML.toString(jsonObject), "</log>\n");
                            TextFileHelper.write(fileName, xml, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    private String getFileName() {
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        return StringUtil.format("{0}/{1}/{2}.{3}", Environment.getExternalStorageDirectory().getPath(), path, DateHelper.getDateWithFormat(new Date(), nameFormat), format.toLowerCase());
    }
}
