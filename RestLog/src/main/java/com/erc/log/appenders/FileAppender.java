package com.erc.log.appenders;

import com.erc.log.AppContext;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.format.LogFormatter;
import com.erc.log.helpers.DateHelper;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.StringUtil;
import com.erc.log.helpers.TextFileHelper;
import com.erc.log.helpers.Util;
import com.erc.log.model.FilesModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class FileAppender extends BaseAppender {

    private String nameFormat;
    private String format;
    private String path;

    public FileAppender() {
    }

    @Override
    public void append(LOG log) {
        String fileName = getFileName();
        boolean existFile = FileHelper.exist(fileName);
        if (!existFile) {
            FilesModel.addFile(fileName);
        }
        if (!hasRoom(fileName)) {
            return;
        }
        switch (fileType()) {
            case TXT:
                writeTxt(fileName, existFile, log);
                break;
            case CSV:
                writeCsv(fileName, existFile, log);
                break;
            case JSON:
                writeJson(fileName, existFile, log);
                break;
            case XML:
                writeXml(fileName, existFile, log);
                break;
        }
    }

    /**
     * Batch write. For the line-based formats (TXT / CSV) the whole batch is written in a
     * single I/O call, and the constant header is emitted only once. JSON / XML keep the
     * per-log append (they grow a single document), still writing the device context once.
     */
    @Override
    public void append(List<LOG> logs) {
        if (logs == null || logs.isEmpty()) {
            return;
        }
        FileType type = fileType();
        if (type == FileType.TXT || type == FileType.CSV) {
            writeLineBatch(type, logs);
        } else {
            for (LOG log : logs) {
                append(log);
            }
        }
    }

    private void writeLineBatch(FileType type, List<LOG> logs) {
        String fileName = getFileName();
        boolean existFile = FileHelper.exist(fileName);
        if (!existFile) {
            FilesModel.addFile(fileName);
        }
        if (!hasRoom(fileName)) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (!existFile) {
            // Constant device/app context, written only once at the top of the file.
            sb.append(LogFormatter.header(logs.get(0))).append('\n');
            if (type == FileType.CSV) {
                sb.append(LogFormatter.csvHeader()).append('\n');
            }
        }
        for (LOG log : logs) {
            if (type == FileType.CSV) {
                sb.append(LogFormatter.csvRow(log)).append('\n');
            } else {
                sb.append(LogFormatter.compactLine(log)).append('\n');
            }
        }
        TextFileHelper.write(fileName, sb.toString(), true);
    }

    private void writeTxt(String fileName, boolean existFile, LOG log) {
        StringBuilder sb = new StringBuilder();
        if (!existFile) {
            sb.append(LogFormatter.header(log)).append('\n');
        }
        sb.append(LogFormatter.compactLine(log)).append('\n');
        TextFileHelper.write(fileName, sb.toString(), true);
    }

    private void writeCsv(String fileName, boolean existFile, LOG log) {
        StringBuilder sb = new StringBuilder();
        if (!existFile) {
            // Constant context once, then the column titles once (never repeated per row).
            sb.append(LogFormatter.header(log)).append('\n');
            sb.append(LogFormatter.csvHeader()).append('\n');
        }
        sb.append(LogFormatter.csvRow(log)).append('\n');
        TextFileHelper.write(fileName, sb.toString(), true);
    }

    private void writeJson(String fileName, boolean existFile, LOG log) {
        Gson gson = new Gson();
        String logJson = gson.toJson(LogFormatter.logMap(log));
        if (existFile) {
            TextFileHelper.deleteLastLine(fileName);
            TextFileHelper.write(fileName, ",\n" + logJson + "\n]}", true);
        } else {
            String deviceJson = gson.toJson(LogFormatter.deviceMap(log));
            TextFileHelper.write(fileName, "{\"device\":" + deviceJson + ",\"logs\":[\n");
            TextFileHelper.write(fileName, logJson + "\n]}", true);
        }
    }

    private void writeXml(String fileName, boolean existFile, LOG log) {
        try {
            Gson gson = new Gson();
            if (!existFile) {
                JSONObject device = new JSONObject(gson.toJson(LogFormatter.deviceMap(log)));
                TextFileHelper.write(fileName, "<device>" + jsonToXml(device) + "</device>\n");
            }
            JSONObject entry = new JSONObject(gson.toJson(LogFormatter.logMap(log)));
            TextFileHelper.write(fileName, "<log>" + jsonToXml(entry) + "</log>\n", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasRoom(String fileName) {
        return FileHelper.getSize(fileName) < LogConfiguration.getInstance(AppContext.getContext()).getMaxFileSizeMb() * 1000;
    }

    private FileType fileType() {
        return FileType.valueOf(format.toUpperCase());
    }

    private String jsonToXml(JSONObject jsonObject) {
        StringBuilder xml = new StringBuilder();
        try {
            java.util.Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    Object value = jsonObject.get(key);
                    xml.append("<").append(key).append(">");
                    if (value instanceof JSONObject) {
                        xml.append(jsonToXml((JSONObject) value));
                    } else {
                        xml.append(value != null ? value.toString() : "");
                    }
                    xml.append("</").append(key).append(">");
                } catch (Exception e) {
                    // Skip invalid entries
                }
            }
        } catch (Exception e) {
            // Return empty if parsing fails
        }
        return xml.toString();
    }

    public String getPath() {
        return path;
    }

    private String getFileName() {
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        return StringUtil.format("{0}/{1}/{2}.{3}",
                Util.getAppPath(AppContext.getContext()) + "files",
                path,
                DateHelper.getDateWithFormat(new Date(), nameFormat),
                format.toLowerCase());
    }
}
