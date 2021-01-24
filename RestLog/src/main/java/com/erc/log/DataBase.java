package com.erc.log;

import com.erc.dal.DB;
import com.erc.dal.upgrade.DBConfig;
import com.erc.log.helpers.Util;

public class DataBase extends DB {
    private static DataBase database = null;
    public static final String DATABASE_NAME = "log.db";
    private static final int DATABASE_VERSION = 1;

    public static DataBase getInstance() {
        if (database == null) {
            DBConfig dbConfig = getDbConfig();
            database = new DataBase(dbConfig);
        }
        return database;
    }

    private static DBConfig getDbConfig() {
        DBConfig dbConfig = new DBConfig(AppContext.getContext(), DATABASE_NAME, DATABASE_VERSION, Util.getAppPath(AppContext.getContext()));
        dbConfig.setPackageFilter("com.erc.log");
        return dbConfig;
    }

    private DataBase(DBConfig dbConfig) {
        super(dbConfig);
    }
}
