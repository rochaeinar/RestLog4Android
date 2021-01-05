package com.erc.log;

import com.erc.dal.DB;
import com.erc.dal.DBConfig;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.Util;

import java.io.File;

public class DataBase extends DB {
    private static DataBase database = null;
    public static final String DATABASE_NAME = "log.db";
    private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_ID = 1;

    public static DataBase getInstance() {
        initDbFile();
        if (database == null) {
            DBConfig dbConfig = getDbConfig();
            database = new DataBase(dbConfig);
        }
        return database;
    }

    private static DBConfig getDbConfig() {
        return new DBConfig(AppContext.getContext(), DATABASE_NAME, DATABASE_VERSION, Util.getAppPath(AppContext.getContext()), DATABASE_ID);
    }

    private DataBase(DBConfig dbConfig) {
        super(dbConfig);
    }

    public static void initDbFile() {
        DBConfig dbConfig = getDbConfig();
        String dbPath = dbConfig.getUrl() + "/" + dbConfig.getDataBaseName();

        File logDbFile = new java.io.File(dbPath);
        if (!logDbFile.exists()) {
            FileHelper.copyRawFileToSdCard(R.raw.log, dbPath, AppContext.getContext());
        }
    }
}
