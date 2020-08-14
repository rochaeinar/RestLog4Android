package com.erc.log;

import android.content.Context;

import com.erc.dal.DB;
import com.erc.dal.DBConfig;

public class DataBase extends DB {
    private static DataBase database = null;
    public static final String DATABASE_NAME = "restlog.db";
    private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_ID = 1;

    public static DataBase getInstance() {
        if (database == null) {
            DBConfig dbConfig = new DBConfig(AppContext.getContext(), DATABASE_NAME, DATABASE_VERSION, "", DATABASE_ID);
            database = new DataBase(dbConfig);
        }
        return database;
    }

    private DataBase(DBConfig dbConfig) {
        super(dbConfig);
    }
}
