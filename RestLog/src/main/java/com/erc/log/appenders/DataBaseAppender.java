package com.erc.log.appenders;

import com.erc.dal.DB;
import com.erc.dal.upgrade.DBConfig;
import com.erc.log.AppContext;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.DateHelper;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.StringUtil;
import com.erc.log.helpers.TextFileHelper;
import com.erc.log.helpers.Util;
import com.erc.log.model.FilesModel;

import java.util.Date;
import java.util.List;

public class DataBaseAppender extends BaseAppender {

    private String nameFormat;
    private String path;

    // Cached open connection, keyed by the (date-based) database file name. These are
    // runtime-only helpers and MUST NOT be serialized with the configuration, hence
    // transient (the configuration is persisted with Gson).
    private transient DB cachedDb;
    private transient String cachedDbName;

    public DataBaseAppender() {
        this.type = AppenderType.DATABASE;
    }

    @Override
    public void append(LOG log) {
        getDb().save(log);
    }

    @Override
    public void append(List<LOG> logs) {
        if (logs == null || logs.isEmpty()) {
            return;
        }
        // Persist the whole batch in a single transaction.
        getDb().saveAll(logs);
    }

    /**
     * Returns a reusable database connection for today's log file. Opening a new
     * {@link DB} (which opens SQLiteOpenHelper) per append was a major cost; we now keep
     * one instance and only recreate it when the target file name changes (e.g. at the
     * daily rollover).
     */
    private DB getDb() {
        ensureFileRegistered();
        String name = getName();
        if (cachedDb == null || !name.equals(cachedDbName)) {
            DBConfig dbConfig = new DBConfig(AppContext.getContext(), name, 1, getPath());
            dbConfig.setPackageFilter("com.erc.log");
            cachedDb = new DB(dbConfig);
            cachedDbName = name;
        }
        return cachedDb;
    }

    private void ensureFileRegistered() {
        TextFileHelper.createParentFolder(getFullPath());
        if (!FileHelper.exist(getFullPath())) {
            FilesModel.addFile(getFullPath());
        }
    }

    private String getFullPath() {
        return StringUtil.format("{0}/{1}", getPath(), getName());
    }

    private String getName() {
        return StringUtil.format("{0}.db", DateHelper.getDateWithFormat(new Date(), nameFormat));
    }

    private String getPath() {
        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf('/'));
        }
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        return StringUtil.format("{0}/{1}", Util.getAppPath(AppContext.getContext()) + "files", path);
    }
}
