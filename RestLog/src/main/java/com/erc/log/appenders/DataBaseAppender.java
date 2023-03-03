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

public class DataBaseAppender extends BaseAppender {

    private String nameFormat;
    private String path;

    public DataBaseAppender() {
        this.type = AppenderType.DATABASE;
    }

    @Override
    public void append(LOG log) {
        TextFileHelper.createParentFolder(getFullPath());
        if (!FileHelper.exist(getFullPath())) {
            FilesModel.addFile(getFullPath());
        }
        DBConfig dbConfig = new DBConfig(AppContext.getContext(), getName(), 1, getPath());
        dbConfig.setPackageFilter("com.erc.log");
        DB db = new DB(dbConfig);
        db.save(log);
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
